package com.example.netweaver.domain.usecase.posts

import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class LikePostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(post: Post): Result<Unit> = try {
        repository.likePost(post)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }
}