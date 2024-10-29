package com.example.netweaver.domain.repository

import com.example.netweaver.domain.model.Post
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getFeedPosts(): Flow<Result<List<Post>>>

}