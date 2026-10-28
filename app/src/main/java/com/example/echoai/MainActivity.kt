package com.example.echoai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var etMessageInput: EditText
    private lateinit var btnSend: Button

    private val messageList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        etMessageInput = findViewById(R.id.etMessageInput)
        btnSend = findViewById(R.id.btnSend)

        // Set up RecyclerView
        chatAdapter = ChatAdapter(messageList)
        recyclerViewChat.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Keeps chat pinned to the bottom as messages grow
        recyclerViewChat.layoutManager = layoutManager

        // Add a welcoming test message from the AI
        messageList.add(ChatMessage("Hello! I am EchoAI. How can I help you today?", false))
        chatAdapter.notifyItemInserted(messageList.size - 1)

        // Handle Send Button Click
        btnSend.setOnClickListener {
            val userText = etMessageInput.text.toString().trim()
            if (userText.isNotEmpty()) {
                // 1. Add User Message
                messageList.add(ChatMessage(userText, true))
                chatAdapter.notifyItemInserted(messageList.size - 1)
                recyclerViewChat.scrollToPosition(messageList.size - 1)
                etMessageInput.text.clear()

                // 2. Simulate an echo response (We will replace this with real API / Gemini next!)
                recyclerViewChat.postDelayed({
                    messageList.add(ChatMessage("Echo: $userText", false))
                    chatAdapter.notifyItemInserted(messageList.size - 1)
                    recyclerViewChat.scrollToPosition(messageList.size - 1)
                }, 1000)
            }
        }
    }
}