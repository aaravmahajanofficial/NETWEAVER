package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Participant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    @SerialName("id")
    val id: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("user_id")
    val participantId: String
)

fun ParticipantDto.toDomain() = Participant(
    id = id,
    chatId = chatId,
    participantId = participantId
)