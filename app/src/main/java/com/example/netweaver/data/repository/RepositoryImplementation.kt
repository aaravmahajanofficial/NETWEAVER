package com.example.netweaver.data.repository

import com.example.netweaver.data.remote.dto.PostDto
import com.example.netweaver.data.remote.dto.UserDto
import com.example.netweaver.data.remote.dto.toDomain
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val postgrest: Postgrest,
    private val firestore: FirebaseFirestore
) :
    Repository {

    override suspend fun getFeedPosts(): Flow<Result<List<Post>>> = callbackFlow {

        val postsRef = firestore.collection("posts")

        val subscription = postsRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }

                if (snapshot != null) {

                    try {

                        launch(Dispatchers.IO) {   // Fetch all the posts from the Firebase

                            val posts =
                                snapshot.documents.mapNotNull { doc ->
                                    doc.toObject(PostDto::class.java)?.copy(id = doc.id)
                                }
                            // get the user ids from posts
                            val userIds = posts.map { it.userId }.distinct()

                            // get the user models and merge each post with user
                            getUsersByIds(userIds).collect { response ->
                                when (response) {
                                    is Result.Success -> {

                                        val usersMap = response.data.associateBy { it.userId }

                                        trySend(Result.Success(posts.map { post ->
                                            post.toDomain()
                                                .copy(user = usersMap[post.userId])
                                        }))
                                    }

                                    is Result.Error -> {
                                        trySend(Result.Error(response.exception))
                                    }
                                }

                            }
                        }

                    } catch (e: Exception) {
                        trySend(Result.Error(e))
                    }
                }

            }

        awaitClose { subscription.remove() }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUsersByIds(userIds: List<String>): Flow<Result<List<User>>> = flow {

        try {
            val response =
                postgrest.from("Users").select {
                    { ("id" in userIds) }
                }.decodeList<UserDto>()

            emit(Result.Success(response.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Result.Error(e))
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