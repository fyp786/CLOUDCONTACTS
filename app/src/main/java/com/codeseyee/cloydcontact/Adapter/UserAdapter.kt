package com.codeseyee.cloydcontact.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeseyee.cloydcontact.Model.User
import com.codeseyee.cloydcontact.R
import com.codeseyee.cloydcontact.databinding.ItemUserBinding

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.name.text = user.name
            binding.role.text = user.role
            binding.department.text = user.department
            // Load image using Glide
            Glide.with(binding.root.context)
                .load(user.profileImageUrl)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .error(R.drawable.placeholder) // Optional error image
                .into(binding.image)
        }
    }
}
