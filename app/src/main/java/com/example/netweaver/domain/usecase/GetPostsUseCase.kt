package com.example.netweaver.domain.usecase

import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Get the posts from the repository
class GetPostsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Result<List<Post>> =
        withContext(Dispatchers.IO) {
            try {
                repository.getFeedPosts().first()
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}