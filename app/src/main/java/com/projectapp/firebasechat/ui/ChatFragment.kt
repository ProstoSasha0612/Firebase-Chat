package com.projectapp.firebasechat.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ChatViewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }
    private var adapter: UserMessageAdapter? = null

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
        setUpOnClickListeners()
        if(savedInstanceState == null){
            setUpActionBarIcon()
        }
    }

    private fun initRecyclerView() {
        binding.rvChat.layoutManager = LinearLayoutManager(this.context)
        adapter = UserMessageAdapter()
        binding.rvChat.adapter = adapter
        binding.rvChat.addItemDecoration(ItemDecoration())

        viewModel.setUpMessageAddingListener(adapter)

    }

    private fun setUpActionBarIcon() {
        val activity = (activity as MainActivity)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val currentUser = Firebase.auth.currentUser

        lifecycleScope.launch {
            val photoDrawable = withContext(Dispatchers.IO) {
                val bitmap = Picasso.get().load(currentUser?.photoUrl).get()
                Log.d("Mytag", "Photo urls is ${currentUser?.photoUrl}")
                val photo = BitmapDrawable(activity.resources, bitmap)
                photo
            }
            activity.supportActionBar?.setHomeAsUpIndicator(photoDrawable)
        }

    }

        private fun setUpOnClickListeners() {
            binding.btnSend.setOnClickListener {
                viewModel.sendMessage(
                    UserMessage(
                        Firebase.auth.currentUser?.displayName ?: "Standard Name",
                        binding.editTextSendMessage.text.toString()
                    )
                )
            }
        }


        companion object {
            fun newInstance() = ChatFragment()
        }

    }