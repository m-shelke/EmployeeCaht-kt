package com.example.employeecahtkt

import android.app.ComponentCaller
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeecahtkt.databinding.ActivitySetUpProfileBinding
import com.example.employeecahtkt.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class SetUpProfileActivity : AppCompatActivity() {

    var binding : ActivitySetUpProfileBinding? = null

    var auth : FirebaseAuth? = null
    var database : FirebaseDatabase? = null
    var storage : FirebaseStorage? = null
    var selectedImage :  Uri? = null
    var dialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)

        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialog = ProgressDialog(this@SetUpProfileActivity)
        dialog!!.setMessage("Uploading Profile....")
        dialog!!.setCancelable(false)
        dialog!!.setIcon(R.drawable.uploading)
        dialog!!.setTitle("Setup in Processing..!!")

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        binding!!.cameraFab.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,45)
        }

        binding!!.setupProfileBtn.setOnClickListener {
            val name : String = binding!!.editName.text.toString()

            if (name.isEmpty()){
                binding!!.editName.setError("Name Please..?")
            }

            dialog!!.show()

            if (selectedImage != null){
                val reference = storage!!.reference.child("profile")
                    .child(auth!!.uid!!)

                reference.putFile(selectedImage!!).addOnCompleteListener { task ->

                    if (task.isSuccessful){
                        reference.downloadUrl.addOnCompleteListener { uri ->

                            val imageUrl = uri.toString()
                            val uid = auth!!.uid
                            val phone = auth!!.currentUser!!.phoneNumber

                            val name:String = binding!!.editName.text.toString()
                            val user = Users(uid,name,phone,imageUrl)

                            database!!.reference!!
                                .child("users")
                                .child(uid!!)
                                .setValue(user)
                                .addOnCompleteListener{
                                    dialog!!.dismiss()

                                    val intent = Intent(this@SetUpProfileActivity,MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }else{

                        Toast.makeText(this@SetUpProfileActivity,"Image Not Selected..!!",Toast.LENGTH_LONG).show()

                        val uid = auth!!.uid
                        val phone = auth!!.currentUser!!.phoneNumber.toString()
                        val name:String = binding!!.editName.text.toString()
                        val users = Users(uid,name,phone,"No Image")

                        database!!.reference
                            .child("users")
                            .child(uid!!)
                            .setValue(users)
                            .addOnCanceledListener{
                                dialog!!.dismiss()

                                val intent = Intent(this@SetUpProfileActivity,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }
                }
            }
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if (data.data != null){
                val uri = data.data   //Filepath
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference
                    .child("profile")
                    .child(time.toString() + "")

                reference.putFile(uri!!).addOnCompleteListener{ task->

                    if (task.isSuccessful){
                        reference.downloadUrl.addOnCompleteListener{ uri->
                            val filepath = uri.toString()
                            val obj = HashMap<String,Any>()
                            obj["image"] = filepath
                            database!!.reference
                                .child("users")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj).addOnSuccessListener {  }
                        }
                    }

                }
            }
            binding!!.profileIv.setImageURI(data.data)
            selectedImage = data.data
        }
    }
}