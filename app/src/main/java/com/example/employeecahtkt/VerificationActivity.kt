package com.example.employeecahtkt

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeecahtkt.databinding.ActivityVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {

     var binding: ActivityVerificationBinding? = null

    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVerificationBinding.inflate(layoutInflater)

        setContentView(binding!!.root)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null){
            val intent = Intent(this@VerificationActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportActionBar?.hide()

        binding!!.phoneNumberEt.requestFocus()

        binding!!.sendOtpBtn.setOnClickListener {

            val intent = Intent(this@VerificationActivity,OTPActivity::class.java)
            intent.putExtra("phoneNumber",binding!!.phoneNumberEt.text.toString())
            startActivity(intent)
        }

    }
}