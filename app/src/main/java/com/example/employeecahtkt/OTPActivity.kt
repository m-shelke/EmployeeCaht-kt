package com.example.employeecahtkt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import com.example.employeecahtkt.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import omari.hamza.storyview.StoryView
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    var binding : ActivityOtpactivityBinding? = null

    var verificationId : String? = null
    var auth : FirebaseAuth? = null
    var dialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)

        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Sending OTP....")
        dialog!!.setCancelable(false)
        dialog!!.setIcon(R.drawable.otp)
        dialog!!.setTitle("OTP Processing..!!")
        dialog!!.show()

        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        var phoneNumber = intent.getStringExtra("phoneNumber")
        binding!!.phoneNumberLable.text = "Verify $phoneNumber"

        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@OTPActivity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    TODO("Not yet implemented")
                }

                override fun onCodeSent(verifyId: String, forceRendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId, forceRendingToken)

                    dialog!!.dismiss()

                    verificationId = verifyId

                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
                    binding!!.otpView.requestFocus()
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        binding!!.otpView.setOtpCompletionListener { otp ->

            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val intent = Intent(this@OTPActivity, SetUpProfileActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }else{

                        Toast.makeText(this@OTPActivity,"Verification Failed..!!",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}