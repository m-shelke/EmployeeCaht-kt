package com.example.employeecahtkt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.employeecahtkt.adapters.MessageAdapter
import com.example.employeecahtkt.databinding.ActivityChatBinding
import com.example.employeecahtkt.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Date

class ChatActivity : AppCompatActivity() {

    var binding: ActivityChatBinding? = null
    var adapter: MessageAdapter? = null
    var messages: ArrayList<Messages>? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var dialog: ProgressDialog? = null
    var senderUid:String? = null
    var receiverUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding!!.toolBar)
//        supportActionBar?.setCustomView(binding!!.toolBar)
     //  supportActionBar?.show()

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        dialog = ProgressDialog(this@ChatActivity)
        dialog!!.setMessage("Uploading Image....")
        dialog!!.setCancelable(false)
        dialog!!.setIcon(R.drawable.uploading)
        dialog!!.setTitle("Image in Processing..!!")

        messages = ArrayList()

        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("image")

        binding!!.profileName.text = name

        Glide.with(this@ChatActivity)
            .load(profile)
            .placeholder(R.drawable.baseline_camera_alt_24)
            .into(binding!!.profileImageView)

        binding!!.toolbarBackBtn.setOnClickListener {
            finish()
        }

        receiverUid = intent.getStringExtra("uid")
        receiverUid = FirebaseAuth.getInstance().uid

        database!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        val status = snapshot.getValue(String::class.java)
                        if (status == "offline") {
                            binding!!.status.visibility = View.GONE
                        } else {
                            binding!!.status.setText(status)
                            binding!!.status.visibility = View.VISIBLE

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) { }
            })

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        adapter = MessageAdapter(this@ChatActivity, messages!!,senderRoom!!,receiverRoom!!)

        binding!!.chatRecyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
        binding!!.chatRecyclerView.adapter = adapter

        database!!.reference.child("chats")
            .child(senderRoom!!)
            .child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messages!!.clear()

                    for (snapshot1 in snapshot.children){
                        val message :Messages? = snapshot1.getValue(Messages::class.java)
                        message!!.messageId = snapshot1.key
                       messages!!.add(message)
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        binding!!.icSendIv.setOnClickListener{

            val messageTxt:String = binding!!.editMessage.text.toString()
            val date = Date()
            val message = Messages(messageTxt,senderUid,date.time)

            binding!!.editMessage.setText("")
            val randomKey = database!!.reference.push().key

            val lastMsgObj = HashMap<String,Any>()
            lastMsgObj["lastMsg"] = message.message!!
            lastMsgObj["lastMsgTime"] = date.time

            database!!.reference.child("chats").child(senderRoom!!)
                .updateChildren(lastMsgObj)

            database!!.reference.child("chats").child(receiverRoom!!)
                .updateChildren(lastMsgObj)

            database!!.reference.child("chats").child(senderRoom!!)
                .child("messages")
                .child(randomKey!!)
                .setValue(message).addOnSuccessListener {                //////// message or  messageText confused
                    database!!.reference.child("chats")
                        .child(receiverRoom!!)
                        .child("message")
                        .child(randomKey)
                        .setValue(message)                           ///// messages or message confused
                        //.addOnSuccessListener {  }
                }

        }

        binding!!.icAttachmentIv.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,25)
        }

        val handler = Handler()

        binding!!.editMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
              database!!.reference.child("Presence")
                 // .child(senderUid!!)
                 // .setValue("typing...")

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping,1000)
            }

            var userStoppedTyping = Runnable {
                database!!.reference.child("Presence")
//                    .child(senderUid!!)
//                    .setValue("Online")
            }

        })

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 25){
            if (data != null){
                if (data.data != null){
                    val selectedImage = data.data
                    val calendar = Calendar.getInstance()
                    var refence = storage!!.reference.child("chats")
                        .child(calendar.timeInMillis.toString()+"")

                    dialog!!.show()

                    refence.putFile(selectedImage!!)
                        .addOnCompleteListener{task->
                            dialog!!.dismiss()

                            if (task.isSuccessful){
                                refence.downloadUrl.addOnSuccessListener {uri->

                                    val filePath = uri.toString()
                                    val messageTxt:String = binding!!.editMessage.text.toString()
                                    val date = Date()
                                    val message = Messages(messageTxt,senderUid,date.time)
                                    message.message = "photo"
                                    message.imageUrl = filePath

                                    binding!!.editMessage.setText("")

                                    val randomKey = database!!.reference.push().key
                                    val lastMsgObj = HashMap<String,Any>()
                                    lastMsgObj["lastMsg"] = message.message!!
                                    lastMsgObj["lastMsgTime"] = date.time
                                    database!!.reference.child("chats")
                                        .updateChildren(lastMsgObj)

                                    database!!.reference.child("chats")
                                        .child(receiverRoom!!)
                                        .updateChildren(lastMsgObj)

                                    database!!.reference.child("chats")
                                        .child(senderRoom!!)
                                        .child("messages")
                                        .child(randomKey!!)
                                        .setValue(message).addOnSuccessListener {

                                            database!!.reference.child("chats")
                                                .child(receiverRoom!!)
                                                .child("messages")
                                                .child(randomKey)
                                                .setValue(messages)
//                                                .addOnSuccessListener {}
                                        }

                                }
                            }
                        }
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()

        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("Online")
    }

    override fun onPause() {
        super.onPause()

        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("offline")
    }

}