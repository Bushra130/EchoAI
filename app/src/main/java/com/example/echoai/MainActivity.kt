package com.example.echoai

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
    }

    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var etMessageInput: EditText
    private lateinit var btnSend: Button
    private lateinit var btnMic: ImageButton

    private val messageList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        etMessageInput = findViewById(R.id.etMessageInput)
        btnSend = findViewById(R.id.btnSend)
        btnMic = findViewById(R.id.btnMic)

        // Set up RecyclerView
        chatAdapter = ChatAdapter(messageList)
        recyclerViewChat.adapter = chatAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Keeps chat pinned to the bottom as messages grow
        recyclerViewChat.layoutManager = layoutManager

        // Add a welcoming test message from the AI
        messageList.add(ChatMessage("Hello! I am EchoAI. How can I help you today?", false))
        chatAdapter.notifyItemInserted(messageList.size - 1)

        // Handle Microphone Click (Speech-to-Text)
        btnMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to EchoAI...")
            }

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show()
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                // Automatically drop the spoken text into your input field
                etMessageInput.setText(result[0])
            }
        }
    }
}