package com.example.employeecahtkt.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.employeecahtkt.ChatActivity
import com.example.employeecahtkt.R
import com.example.employeecahtkt.databinding.ItemProfileBinding
import com.example.employeecahtkt.models.Users

class UsersAdapter(var context: Context,var userList: ArrayList<Users>) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    inner  class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding : ItemProfileBinding = ItemProfileBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_profile,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        var position = userList[position]

        holder.binding.userName.text = position.name

        Glide.with(context)
            .load(position.profileImage)
            .placeholder(R.drawable.person)
            .into(holder.binding.profileIv)

        holder.itemView.setOnClickListener{
            var intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",position.name)
            intent.putExtra("image",position.profileImage)
            intent.putExtra("uid",position.uid)

            context.startActivity(intent)
        }
    }
}