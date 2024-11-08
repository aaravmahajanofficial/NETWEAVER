package com.example.netweaver.data.repository

import android.util.Log
import com.example.netweaver.data.remote.dto.PostDto
import com.example.netweaver.data.remote.dto.UserDto
import com.example.netweaver.data.remote.dto.toDomain
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System.now
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val postgrest: Postgrest,
    private val supabaseStorage: Storage,
    private val firestore: FirebaseFirestore
) :
    Repository {

    override suspend fun getFeedPosts(): Flow<Result<List<Post>>> = callbackFlow {

        val postsRef = firestore.collection("posts")

        val subscription = postsRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {

                    try {

                        launch(Dispatchers.IO) {

                            // Fetch all the posts from the Firebase
                            val posts =
                                snapshot.documents.mapNotNull { doc ->
                                    doc.toObject(PostDto::class.java)?.copy(docId = doc.id)
                                }

                            // get the user ids from posts
                            val userIds = posts.map { it.userId }.distinct()
                            val userResponse = getUsersByIds(userIds)

                            // fetch like statuses for the posts for the current user
                            val postIds = posts.map { it.id }.distinct()
                            val likedPostsResponse =
                                getLikesForPosts("0f07b4e0-4eb8-4a9a-be40-07ae8f608b0e", postIds)

                            if (userResponse is Result.Success && likedPostsResponse is Result.Success) {

                                val usersMap = userResponse.data.associateBy { it.userId }
                                val likedPostsIds = likedPostsResponse.data

                                Log.d("Liked POSTS ID", likedPostsIds.toString())
                                Log.d("POSTS ID", postIds.toString())

                                val finalPosts = posts.map { post ->
                                    post.toDomain().copy(
                                        user = usersMap[post.userId],
                                        isLiked = likedPostsIds.contains(post.id)
                                    )
                                }

                                Log.d("Final POSTS", finalPosts.toString())

                                trySend(Result.Success(finalPosts))
                            } else {
                                val error = (userResponse as Result.Error).exception
                                close(error)
                            }
                        }

                    } catch (e: Exception) {
                        Log.d("getPosts", e.toString())
                        trySend(Result.Error(e))
                        close(e)
                    }
                }

            }

        awaitClose { subscription.remove() }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUsersByIds(userIds: List<String>): Result<List<User>> =
        withContext(Dispatchers.IO) {
            try {
                val response =
                    postgrest.from("Users").select {
                        { ("id" in userIds) }
                    }.decodeList<UserDto>()

                Result.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                Result.Error(e)
            }

        }

    override suspend fun getLikesForPosts(
        userId: String,
        postIds: List<String>
    ): Result<Set<String>> =

        withContext(Dispatchers.IO) {
            try {

                val result =
                    postgrest.from("Likes").select(columns = Columns.list("post_id")) {
                        filter {
                            eq("user_id", userId)
                        }
                        filter {
                            "post_Id" in postIds
                        }
                    }.decodeList<Map<String, String>>()

                Log.d("TAG", "Successful $result")

                val likedPostIds = result.map { it["post_id"] as String }.toSet()

                Result.Success(likedPostIds)

            } catch (e: Exception) {
                Log.d("TAG ERROR", e.toString())
                Result.Error(e)
            }

        }

    override suspend fun storeToBucket(
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<List<String>?> =
        try {

            withContext(Dispatchers.IO) {

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
            }

        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun getUserById(userId: String): Result<User> = try {
        withContext(Dispatchers.IO) {
            val response = postgrest.from("Users")
                .select { filter { eq("id", userId) } }
                .decodeSingle<UserDto>()

            Result.Success(response.toDomain())
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun upsertUser(user: User): Result<User> = try {

        withContext(Dispatchers.IO) {
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
        }

    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun createPost(
        content: String,
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<Unit> =
        try {
            when (val response =
                storeToBucket(byteArrayList = byteArrayList, fileExtensions = fileExtensions)) {
                is Result.Error -> {
                    response.exception
                }

                is Result.Success -> {

                    val postDto = PostDto(
                        userId = UUID.randomUUID().toString(),
                        content = content.trim(),
                        mediaUrl = response.data,
                        createdAt = Timestamp.now(),
                        updatedAt = Timestamp.now()
                    )

                    withContext(Dispatchers.IO) {
                        firestore.collection("posts").add(postDto).await()
                    }
                }

            }

            Result.Success(Unit)

        } catch (e: IllegalArgumentException) {
            Result.Error(e)
        } catch (e: FirebaseFirestoreException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun likePost(post: Post): Result<Unit> = withContext(Dispatchers.IO) {

        val postDto = PostDto(
            id = post.id,
            userId = post.user?.userId
                ?: return@withContext Result.Error(IllegalArgumentException("User cannot be null")),
            content = post.content,
            mediaUrl = post.mediaUrl,
            likesCount = post.likesCount + 1,
            commentsCount = post.commentsCount,
            createdAt = Timestamp(Date(post.createdAt.toEpochMilliseconds())),
            updatedAt = Timestamp(Date(now().toEpochMilliseconds()))
        )

        try {

            firestore.collection("posts").document(postDto.id)
                .set(postDto, SetOptions.merge()).await()

            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }
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

data class Document(
    val byteArray: ByteArray?,
    var fileExtension: String,
)