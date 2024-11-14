package com.example.netweaver.domain.repository

import com.example.netweaver.domain.model.Connection
import com.example.netweaver.domain.model.Education
import com.example.netweaver.domain.model.Experience
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow

interface Repository {

    // Feed & Post
    suspend fun getPosts(): Flow<Result<List<Post>>>
    suspend fun likePost(post: Post): Result<Unit>
    suspend fun unlikePost(post: Post): Result<Unit>
    suspend fun getLikesForPosts(postIds: List<String>): Result<Set<String>>
    suspend fun createPost(
        content: String,
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<Unit>

    suspend fun getUserPosts(userId: String): Result<List<Post>>

    // User
    suspend fun getUsersByIds(userIds: List<String>): Result<List<User>>
    suspend fun getUserById(userId: String): Result<User>
    suspend fun upsertUser(user: User): Result<User>

    // Experience Section
    suspend fun getExperiences(userId: String): Result<List<Experience>>

    // Education Section
    suspend fun getEducation(userId: String): Result<List<Education>>

    // MISC
    suspend fun storeMediaToBucket(
        byteArray: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<List<String>?>

//    suspend fun sharePost(postId: String): Result<Unit>

    // Network/Connections
    suspend fun sendConnectionRequest(receiverId: String): Result<Unit>
    suspend fun acceptConnectionRequest(requestId: String): Result<Unit>
    suspend fun rejectConnectionRequest(requestId: String): Result<Unit>
    suspend fun getConnections(userId: String): Result<List<User>>
    suspend fun getPendingConnections(userId: String): Result<List<User>>
    suspend fun getConnectionStatus(userId: String): Result<Connection>
}