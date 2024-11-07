package com.example.netweaver.domain.usecase.posts

import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(
        content: String,
        byteArrayList: List<ByteArray?>?,
        fileExtensions: List<String?>
    ): Result<Unit> =
        try {

            repository.createPost(content, byteArrayList, fileExtensions)

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }

}