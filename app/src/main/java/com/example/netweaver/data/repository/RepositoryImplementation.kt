package com.example.netweaver.data.repository

import android.util.Log
import com.example.netweaver.data.remote.dto.PostDto
import com.example.netweaver.data.remote.dto.toDomain
import com.example.netweaver.domain.model.Post
import com.example.netweaver.domain.repository.Repository
import com.example.netweaver.ui.model.Result
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(private val postgrest: Postgrest) :
    Repository {

    override suspend fun getFeedPosts(): Flow<Result<List<Post>>> = flow {
        Log.d("DATA", "Attempting to log response data")
        val response =
            postgrest.from("Posts").select(
                Columns.raw("*, user:user_id(*)")
            ).decodeList<PostDto>()

        emit(Result.Success(response.map { it.toDomain().copy(user = it.user?.toDomain()) }))
    }.catch { e ->
        Result.Error(e)
    }

}