package com.example.employeecahtkt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.employeecahtkt.R
import com.example.employeecahtkt.databinding.DeleteLayoutBinding
import com.example.employeecahtkt.databinding.ReceiveSmsBinding
import com.example.employeecahtkt.databinding.SendSmsBinding
import com.example.employeecahtkt.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(var context: Context, messages: ArrayList<Messages>?, senderRoom: String, receiverRoom: String) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(){

    lateinit var messages: ArrayList<Messages>

    val ITEM_SEND = 1
    val ITEM_RECEIVED = 2
    val senderRoom: String
    val receiverRoom: String



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_SEND){
            val view = LayoutInflater.from(context).inflate(R.layout.send_sms,parent,false)
            SentMsgHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.receive_sms,parent,false)
            return ReceivedMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]

        return if(FirebaseAuth.getInstance().uid == messages.senderId){
            ITEM_SEND
        }else{
            ITEM_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]

        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder

            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE

                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.baseline_camera_alt_24)
                    .into(viewHolder.binding.image)
            }

            viewHolder.binding.message.text = message.message

            viewHolder.itemView.setOnLongClickListener {

                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Messages")
                    .setMessage("Make sure, Is Important..?")
                    .setIcon(R.drawable.delete)
                    .setView(binding.root)
                    .create()

                binding.everyOneTv.setOnClickListener {
                    message.message = "This message is Removed..!!"

                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }

                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1!!).setValue(message)
                    }
                    dialog.dismiss()
                }

                binding.fromMeTv.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancleTv.setOnClickListener { dialog.dismiss() }
                dialog.show()

                false
            }
        }
        else {

            val viewHolder = holder as ReceivedMsgHolder

            if (message.message.equals("photo")) {

                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE

                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.baseline_camera_alt_24)
                    .into(viewHolder.binding.image)
            }

            viewHolder.binding.message.text = message.message

            viewHolder.itemView.setOnLongClickListener {

                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Messages")
                    .setMessage("Make sure, Is Important..?")
                    .setIcon(R.drawable.delete)
                    .setView(binding.root)
                    .create()

                binding.everyOneTv.setOnClickListener {
                    message.message = "This message is Removed..!!"

                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }

                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1!!).setValue(message)
                    }
                    dialog.dismiss()
                }

                binding.fromMeTv.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancleTv.setOnClickListener { dialog.dismiss() }
                dialog.show()

                false
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SentMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding : SendSmsBinding = SendSmsBinding.bind(itemView)
    }

    inner class ReceivedMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding : SendSmsBinding = SendSmsBinding.bind(itemView)
    }

    init {
        if (messages != null){
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }



}