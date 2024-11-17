package com.example.netweaver.data.repository

import com.example.netweaver.data.remote.dto.ConnectionDto
import com.example.netweaver.data.remote.dto.ConnectionStatus
import com.example.netweaver.data.remote.dto.EducationDto
import com.example.netweaver.data.remote.dto.ExperienceDto
import com.example.netweaver.data.remote.dto.LikeDto
import com.example.netweaver.data.remote.dto.PostDto
import com.example.netweaver.data.remote.dto.UserDto
import com.example.netweaver.data.remote.dto.toDomain
import com.example.netweaver.data.remote.dto.toDomainModel
import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.model.Education
import com.example.netweaver.domain.model.Experience
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.ConnectionType
import com.example.netweaver.domain.repository.Document
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System.now
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.distinct
import kotlin.collections.map

class RepositoryImplementation @Inject constructor(
    private val postgrest: Postgrest,
    private val supabaseStorage: Storage,
    supabaseClient: SupabaseClient,
    firebaseAuth: FirebaseAuth
) :
    Repository {

    private val currentUserId =
        firebaseAuth.currentUser?.uid ?: "0f07b4e0-4eb8-4a9a-be40-07ae8f608b0e"

    private val channel = supabaseClient.channel("postsChannel")

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPosts(): Flow<Result<List<Post>>> =

        channel.postgresListDataFlow(
            schema = "public",
            table = "Posts",
            primaryKey = PostDto::id
        ).onStart {
            channel.subscribe()
        }
            .transformLatest { posts ->

                try {
                    // Parallel Process -> get the user ids from posts & fetch like statuses for the posts for the current user
                    coroutineScope {
                        val usersDeferred = async {
                            getUsersByIds(posts.map { it.userId }
                                .distinct())
                        }
                        val likedPostsDeferred =
                            async { getLikesForPosts(posts.map { it.id }.distinct()) }

                        val usersResponse = usersDeferred.await()
                        val likedPostsResponse = likedPostsDeferred.await()

                        when {
                            (usersResponse is Result.Success) && (likedPostsResponse is Result.Success) -> {

                                val usersMap = usersResponse.data.associateBy { it.userId }

                                val likedPostsIds = likedPostsResponse.data

                                val finalPosts = posts.map { post ->
                                    post.toDomain().copy(
                                        user = usersMap[post.userId],
                                        isLiked = likedPostsIds.contains(post.id)
                                    )
                                }

                                emit(Result.Success(finalPosts))
                            }

                            else -> {
                                emit(Result.Error(Exception("Failed to fetch the users or likes. Try again")))
                            }
                        }
                    }
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }.catch { e ->
                emit(Result.Error(Exception(e)))
            }.onCompletion {
                channel.unsubscribe()
            }

    override suspend fun getUsersByIds(userIds: List<String>?): Result<List<User>> =
        withContext(Dispatchers.IO) {

            try {
                if (userIds.isNullOrEmpty()) {
                    return@withContext Result.Success(emptyList())
                }

                val response =
                    postgrest.from("Users").select {
                        {
                            filter {
                                filter("id", operator = FilterOperator.IN, userIds)
                            }
                        }
                    }.decodeList<UserDto>()

                Result.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.Error(e)
            }

        }

    override suspend fun getLikesForPosts(
        postIds: List<String>
    ): Result<Set<String>> = withContext(Dispatchers.IO) {
        try {

            val result =
                postgrest.from("Likes").select(columns = Columns.list("post_id")) {
                    filter {
                        eq("user_id", currentUserId)
                    }
                    filter {
                        "post_Id" in postIds
                    }
                }.decodeList<Map<String, String>>()

            val likedPostIds = result.map { it["post_id"] as String }.toSet()

            Result.Success(likedPostIds)

        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun storeMediaToBucket(
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<List<String>?> = withContext(Dispatchers.IO) {
        try {
            val result = byteArrayList?.mapIndexed { index, array ->
                Document(
                    byteArray = array,
                    fileExtension = fileExtensions.getOrNull(index) ?: ""
                )
            }

            val urls = result?.mapNotNull { document ->
                val byteArray = document.byteArray ?: return@mapNotNull null
                val uniqueFileName = "${UUID.randomUUID()}.${document.fileExtension}"

                val response = supabaseStorage.from("MediaCollection")
                    .upload(uniqueFileName, byteArray) {
                        upsert = true
                    }

                supabaseStorage.from("MediaCollection").publicUrl(response.path)
            }

            Result.Success(urls)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = postgrest.from("Users")
                .select { filter { eq("id", userId) } }
                .decodeSingle<UserDto>()

            Result.Success(response.toDomain())

        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    override suspend fun upsertUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = postgrest.from("Users").upsert(
                UserDto(
                    userId = user.userId,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    createdAt = now(),
                    updatedAt = now(),
                )
            ) { select() }.decodeSingle<UserDto>()

            Result.Success(response.toDomain())

        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    override suspend fun createPost(
        content: String,
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            val response =
                storeMediaToBucket(
                    byteArrayList = byteArrayList,
                    fileExtensions = fileExtensions
                )

            when (response) {
                is Result.Error -> {
                    return@withContext Result.Error(response.exception)
                }

                is Result.Success -> {

                    val postDto = PostDto(
                        userId = currentUserId,
                        content = content.trim(),
                        mediaUrl = response.data,
                    )

                    try {
                        postgrest.from("Posts").upsert(postDto)
                        return@withContext Result.Success(Unit)
                    } catch (e: Exception) {
                        return@withContext Result.Error(e)
                    }
                }

            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }

    }

    override suspend fun likePost(post: Post): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            val likeDto = LikeDto(
                userId = currentUserId,
                postId = post.id,
                createdAt = now()
            )
            postgrest.from("Likes").upsert(likeDto)
            postgrest.from("Posts").update({
                set("likes_count", post.likesCount + 1)
            }) {
                filter { eq("id", post.id) }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    override suspend fun unlikePost(post: Post): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {

                postgrest.from("Posts").update({
                    set("likes_count", post.likesCount - 1)
                }) {
                    filter { eq("id", post.id) }
                }

                postgrest.from("Likes").delete {
                    filter {
                        and {
                            eq("user_id", currentUserId)
                            eq("post_id", post.id)
                        }
                    }
                }

                Result.Success(Unit)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getUserPosts(userId: String): Result<List<Post>> =
        withContext(Dispatchers.IO) {
            try {
                val response = postgrest.from("Posts").select {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeList<PostDto>()

                Result.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun getExperiences(userId: String): Result<List<Experience>> =
        withContext(Dispatchers.IO) {
            try {
                val response = postgrest.from("Experience").select {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeList<ExperienceDto>()

                Result.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun getEducation(userId: String): Result<List<Education>> =
        withContext(Dispatchers.IO) {
            try {
                val response = postgrest.from("Education").select {
                    filter {
                        eq("user_id", userId)
                    }
                }.decodeList<EducationDto>()

                Result.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


    override suspend fun sendConnectionRequest(userId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                postgrest.from("Connections").upsert(
                    ConnectionDto(
                        requesterId = currentUserId,
                        receiverId = userId
                    )
                )

                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun acceptConnectionRequest(requestId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                postgrest.from("Connections").update(
                    {
                        set("status", ConnectionStatus.CONNECTED)
                    }
                ) {
                    filter {
                        eq("id", requestId)
                        eq("receiver_id", currentUserId)
                    }
                }

                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }

        }

    override suspend fun rejectConnectionRequest(requestId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                postgrest.from("Connections").delete {
                    filter {
                        eq("id", requestId)
                        eq("receiver_id", currentUserId)
                    }
                }

                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }

        }

    override suspend fun getConnections(userId: String): Result<List<Connection>> =
        withContext(Dispatchers.IO) {
            try {
                val connections =
                    postgrest.from("Connections")
                        .select {
                            filter {
                                and {
                                    or {
                                        eq("receiver_id", userId)
                                        eq("requester_id", userId)
                                    }
                                    eq("status", ConnectionStatus.CONNECTED)
                                }
                            }
                        }
                        .decodeList<ConnectionDto>().map { it.toDomainModel() }


//                val users = getUsersByIds(connectedUserIds)
//
//                when (users) {
//                    is Result.Success -> {
//
//                        val updatedConnections = connections.map { connection ->
//
//                            val otherUser = if (connection.requesterId == userId) {
//                                connection.receiverId
//                            } else {
//                                connection.requesterId
//                            }
//
//                            val user = users.data.find { it.userId == otherUser }
//
//                            connection.copy(user = user)
//                        }
//
//
//                        Result.Success(updatedConnections)
//                    }
//
//                    is Result.Error -> {
//                        Result.Error(users.exception)
//                    }
//                }

                Result.Success(connections)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getConnectionRequests(connectionType: ConnectionType): Result<List<Connection>> =
        withContext(Dispatchers.IO) {
            try {
                val connections =
                    postgrest.from("Connections")
                        .select {
                            filter {
                                and {
                                    when (connectionType) {
                                        is ConnectionType.All -> {
                                            or {
                                                eq("receiver_id", currentUserId)
                                                eq("requester_id", currentUserId)
                                            }
                                        }

                                        is ConnectionType.IncomingOnly -> {
                                            eq("receiver_id", currentUserId)
                                        }
                                    }
                                    eq("status", ConnectionStatus.PENDING)
                                }
                            }
                        }
                        .decodeList<ConnectionDto>().map { it.toDomainModel() }

                when (connectionType) {
                    ConnectionType.All -> {

                        val users = getUsersByIds(extractUserIds(connections, currentUserId))

                        when (users) {
                            is Result.Success -> {

                                val updatedConnections = connections.map { connection ->

                                    val user =
                                        users.data.find { it.userId == connection.receiverId || it.userId == connection.requesterId }

                                    connection.copy(user = user)
                                }

                                Result.Success(updatedConnections)
                            }

                            is Result.Error -> {
                                Result.Error(users.exception)
                            }
                        }
                    }

                    ConnectionType.IncomingOnly -> {
                        val users = getUsersByIds(connections.map { it.requesterId })
                        when (users) {
                            is Result.Success -> {

                                val updatedConnections = connections.map { connection ->

                                    val user =
                                        users.data.find { it.userId == connection.requesterId }

                                    connection.copy(user = user)

                                }
                                Result.Success(updatedConnections)
                            }

                            is Result.Error -> {
                                Result.Error(users.exception)
                            }

                        }
                    }
                }

            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getConnectionsCount(userId: String): Result<Long> =
        withContext(Dispatchers.IO) {
            try {
                val count = postgrest.from("Connections").select {
                    count(Count.EXACT)
                    filter {
                        and {
                            or {
                                eq("receiver_id", userId)
                                eq("requester_id", userId)
                            }
                            eq("status", ConnectionStatus.CONNECTED)

                        }
                    }
                }.countOrNull()

                Result.Success(count ?: 0)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getConnectionStatus(userId: String): Result<Connection?> =
        withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("Connections").select {
                    filter {
                        or {
                            and {
                                eq("receiver_id", currentUserId)
                                eq("requester_id", userId)
                            }

                            and {
                                eq("receiver_id", userId)
                                eq("requester_id", currentUserId)
                            }
                        }
                    }
                }.decodeSingleOrNull<ConnectionDto>()

                Result.Success(result?.toDomainModel())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getRecommendations(count: Long?): Result<List<User>> =
        withContext(Dispatchers.IO) {
            try {

                val (connectedUserIds, pendingConnections) = coroutineScope {
                    val connectionsDeferred = async {
                        when (val result = getConnections(currentUserId)) {
                            is Result.Success -> extractUserIds(result.data, currentUserId)
                            is Result.Error -> emptySet()
                        }
                    }
                    val pendingConnectionsDeferred =
                        async {
                            when (val result =
                                getConnectionRequests(connectionType = ConnectionType.All)) {
                                is Result.Success -> extractUserIds(result.data, currentUserId)
                                is Result.Error -> emptySet()
                            }
                        }
                    Pair(connectionsDeferred.await(), pendingConnectionsDeferred.await())
                }

                val excludedIds = buildSet(connectedUserIds.size + pendingConnections.size + 1) {
                    addAll(connectedUserIds)
                    addAll(pendingConnections)
                    add(currentUserId)
                }

                val users =
                    postgrest.from("Users").select {
                        filter {
                            filterNot(
                                "id",
                                FilterOperator.IN,
                                "(${excludedIds.joinToString(",")})"
                            )
                        }
                    }.decodeList<UserDto>()

                Result.Success(users.map { it.toDomain() })
            } catch (
                e: Exception
            ) {
                Result.Error(e)
            }
        }
}

private fun extractUserIds(
    connections: List<Connection>,
    userId: String
): List<String> {
    val connectedUserIds = connections.flatMap { connection ->
        listOf(connection.requesterId, connection.receiverId)
    }.filter {
        it != userId
    }
    return connectedUserIds
}


// // Let's say response.data has these users:
//[
//    User(userId: "123", name: "John"),
//    User(userId: "456", name: "Jane")
//]
//
//// associateBy { it.userId } converts it to:
//{
//    "123" -> User(userId: "123", name: "John"),
//    "456" -> User(userId: "456", name: "Jane")
//}