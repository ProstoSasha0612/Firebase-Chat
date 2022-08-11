package com.projectapp.firebasechat.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.UserMessage
import com.projectapp.firebasechat.databinding.FragmentChatBinding
import com.projectapp.firebasechat.recyclerview.ItemDecoration
import com.projectapp.firebasechat.recyclerview.UserMessageAdapter
import com.projectapp.firebasechat.viewmodel.ChatViewModel
import com.squareup.picasso.Picasso

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ChatViewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }
    private lateinit var adapter: UserMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initReceiveMessageListener()

        setUpActionBarIcon()
        binding.btnSend.setOnClickListener {
            sendMessage(
                UserMessage(
                    Firebase.auth.currentUser?.displayName ?: "Standard Name",
                    binding.editTextSendMessage.text.toString()
                )
            )
        }
    }

    private fun initRecyclerView() {
        binding.rvChat.layoutManager = LinearLayoutManager(this.context)
        adapter = UserMessageAdapter()
        binding.rvChat.addItemDecoration(ItemDecoration())


        binding.rvChat.adapter = adapter
    }

    private fun initReceiveMessageListener() {
        val ref = Firebase.database.getReference("messages")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserMessage?>()
                for (s in snapshot.children) {
                    list.add(s.getValue<UserMessage>())
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Mytag", error.message)
            }
        })
    }

    private fun sendMessage(userMessage: UserMessage) {
        val ref = Firebase.database.getReference("messages")
        ref.child(ref.push().key ?: "null key").setValue(userMessage)
    }

    private fun setUpActionBarIcon() {
        val activity = (activity as MainActivity)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val currentUser = Firebase.auth.currentUser
        Thread {
            val bitmap = Picasso.get().load(currentUser?.photoUrl).get()
            Log.d("Mytag","Photo urls is ${currentUser?.photoUrl}")
            val photo = BitmapDrawable(resources, bitmap)

            requireActivity().runOnUiThread {
                activity.supportActionBar?.setHomeAsUpIndicator(photo)
            }
        }.start()

    }


    companion object {
        fun newInstance() = ChatFragment()
    }

}