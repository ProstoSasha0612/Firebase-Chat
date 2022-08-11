package com.projectapp.firebasechat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectapp.firebasechat.UserMessage
import com.projectapp.firebasechat.databinding.UserMessageViewHolderBinding

class UserMessageAdapter : ListAdapter<UserMessage, UserMessageAdapter.UserMessageViewHolder>(
    userMessageDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMessageViewHolder {
        return UserMessageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserMessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserMessageViewHolder(private val binding: UserMessageViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userMessage: UserMessage) {
            binding.tvName.text = userMessage.name
            binding.tvMessage.text = userMessage.message
        }

        companion object {
            fun create(parent: ViewGroup): UserMessageViewHolder {
                val binding =
                    UserMessageViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return UserMessageViewHolder(binding)
            }
        }
    }

    companion object {
        val userMessageDiffUtil = object : DiffUtil.ItemCallback<UserMessage>() {
            override fun areItemsTheSame(oldItem: UserMessage, newItem: UserMessage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserMessage, newItem: UserMessage): Boolean {
                return oldItem == newItem
            }
        }
    }

}