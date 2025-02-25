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
import com.example.employeecahtkt.models.MessageModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(var context: Context,messageList: ArrayList<MessageModel>,senderRoom: String,receiverRoom: String) : RecyclerView.Adapter<RecyclerView.ViewHolder?>(){

    lateinit var messageList: ArrayList<MessageModel>

    val ITEM_SEND = 1
    val ITEM_RECEIVED = 2
    val senderRoom: String
    val receiverRoom: String


    inner class SentMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding : SendSmsBinding = SendSmsBinding.bind(itemView)
    }

    init {
        if (messageList != null){
            this.messageList = messageList
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }

    inner class ReceivedMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding : ReceiveSmsBinding = ReceiveSmsBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == ITEM_SEND){
            val view: View = LayoutInflater.from(context).inflate(R.layout.send_sms,parent,false)
            SentMsgHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive_sms,parent,false)
            return ReceivedMsgHolder(view)
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val messageList = messageList[position]

        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder

            if (messageList.message.equals("photo")) {
                viewHolder.binding.placeholderIv.visibility = View.VISIBLE
                viewHolder.binding.senMessage.visibility = View.GONE
                viewHolder.binding.senLinear.visibility = View.GONE

                Glide.with(context)
                    .load(messageList.imageUrl)
                    .placeholder(R.drawable.baseline_camera_alt_24)
                    .into(viewHolder.binding.placeholderIv)
            }

            viewHolder.binding.senMessage.text = messageList.message

            viewHolder.itemView.setOnClickListener {

                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setMessage("Make sure, Is Important..?")
                    .setIcon(R.drawable.delete)
                    .create()

                binding.everyOneTv.setOnClickListener {
                    messageList.message = "This message is Removed..!!"

                    messageList.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(messageList)
                    }

                    messageList.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1!!).setValue(messageList)
                    }

                    dialog.dismiss()
                }

                binding.fromMeTv.setOnClickListener {
                    messageList.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancleTv.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()

                false
            }

        } else {

            val viewHolder = holder as ReceivedMsgHolder

            if (messageList.message.equals("photo")) {

                viewHolder.binding.placeholderIv.visibility = View.VISIBLE
                viewHolder.binding.recMessage.visibility = View.GONE
                viewHolder.binding.recLinear.visibility = View.GONE

                Glide.with(context)
                    .load(messageList.imageUrl)
                    .placeholder(R.drawable.baseline_camera_alt_24)
                    .into(viewHolder.binding.placeholderIv)
            }

            viewHolder.binding.recMessage.text = messageList.message

            viewHolder.itemView.setOnClickListener {

                val view: View = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)

                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setMessage("Make sure, Is Important..?")
                    .setIcon(R.drawable.delete)
                    .create()

                binding.everyOneTv.setOnClickListener {
                    messageList.message = "This message is Removed..!!"

                    messageList.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(messageList)
                    }

                    messageList.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1!!).setValue(messageList)
                    }

                    dialog.dismiss()
                }

                binding.fromMeTv.setOnClickListener {
                    messageList.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancleTv.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()

                false
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messageList = messageList[position]

        return if(FirebaseAuth.getInstance().uid == messageList.senderId){
            ITEM_SEND
        }else{
            ITEM_RECEIVED
        }
    }
}