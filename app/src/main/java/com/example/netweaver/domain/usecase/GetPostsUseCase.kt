package com.example.netweaver.domain.usecase

import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Get the posts from the repository
class GetPostsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Result<List<Post>>> = repository.getFeedPosts()

}