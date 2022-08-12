package com.projectapp.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.UserMessage
import com.projectapp.firebasechat.recyclerview.UserMessageAdapter

class ChatViewModel : ViewModel() {
    private val ref = Firebase.database.getReference(MESSAGES_REFERENCE)

    fun sendMessage(userMessage: UserMessage) {
        ref.child(ref.push().key ?: "null key").setValue(userMessage)
    }

    fun setUpMessageAddingListener(adapter: UserMessageAdapter?) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserMessage?>()
                for (s in snapshot.children) {
                    list.add(s.getValue<UserMessage>())
                }
                adapter?.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Mytag", error.message)
            }
        })
    }

    companion object {
        const val MESSAGES_REFERENCE = "messages"
    }
}