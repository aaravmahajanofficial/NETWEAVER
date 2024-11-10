package com.example.netweaver.domain.usecase.user

import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(userId: String): Result<List<Post>> =
        repository.getUserPosts(userId = userId)
}