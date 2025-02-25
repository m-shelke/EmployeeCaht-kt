package com.example.employeecahtkt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.employeecahtkt.adapters.UsersAdapter
import com.example.employeecahtkt.databinding.ActivityMainBinding
import com.example.employeecahtkt.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? = null

    var database : FirebaseDatabase? = null
    var usersList : ArrayList<Users>? = null
    var usersAdapter : UsersAdapter? = null
    var dialog : ProgressDialog? = null
    var users : Users? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding!!.main.setOnClickListener{
            startActivity(Intent(this@MainActivity,ChatActivity::class.java))
        }





        dialog = ProgressDialog(this@MainActivity)
        dialog!!.setMessage("Uploading Images....")
        dialog!!.setCancelable(false)
        dialog!!.setIcon(R.drawable.proccess)
        dialog!!.setTitle("UI in Processing..!!")

        database = FirebaseDatabase.getInstance()

        usersList = ArrayList<Users>()
        usersAdapter = UsersAdapter(this@MainActivity,usersList!!)

        val layoutManager = GridLayoutManager(this@MainActivity,2)
        binding!!.mRec.layoutManager = layoutManager

        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    users = snapshot.getValue(Users::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding!!.mRec.adapter = usersAdapter

        database!!.reference.child("users").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                usersList!!.clear()

                for (snapshot in snapshot.children){

                    val users : Users? = snapshot.getValue(Users::class.java)

                    if (!users!!.uid.equals(FirebaseAuth.getInstance().uid)) usersList!!.add(users)
                }

                usersAdapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onResume() {
        super.onResume()

        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()

        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Offline")

    }
}