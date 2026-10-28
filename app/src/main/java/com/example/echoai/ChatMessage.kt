package com.example.echoai

data class ChatMessage(
    val message: String,
    val isUser: Boolean // true if sent by user, false if replied by AI
)