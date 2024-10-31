package com.example.netweaver.domain.repository

import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.model.User
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getUsersByIds(userIds: List<String>): Flow<Result<List<User>>>

    suspend fun getFeedPosts(): Flow<Result<List<Post>>>

//    suspend fun likePost(postId: String): Result<Unit>
//
//    suspend fun unlikePost(postId: String): Result<Unit>
//
//    suspend fun sharePost(postId: String): Result<Unit>
}