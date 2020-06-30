package com.tahadroid.messenger

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), TextWatcher {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btn_sign_in.setOnClickListener {
            val email = editText_email_sign_in.text.toString().trim()
            val password = editText_password_sign_in.text.toString().trim()
            if (checkValidate(email, password)) return@setOnClickListener
            progressBar_sign_in.visibility= View.VISIBLE
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar_sign_in.visibility= View.GONE
                    val intentMianActivity = Intent(this@SignInActivity, MainActivity::class.java)
                    intentMianActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intentMianActivity)
                } else {
                    progressBar_sign_in.visibility= View.GONE
                    Toast.makeText(this@SignInActivity, task.exception?.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        editText_email_sign_in.addTextChangedListener(this@SignInActivity)
        editText_password_sign_in.addTextChangedListener(this@SignInActivity)
        btn_create_account.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser?.uid != null) {
            val intentMianActivity = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intentMianActivity)
        }
    }

    private fun checkValidate(
        email: String,
        password: String
    ): Boolean {

        if (email.isEmpty()) {
            editText_email_sign_in.error = "Enter email"
            editText_email_sign_in.requestFocus()
            return true
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_email_sign_in.error = "Invalid email"
            editText_email_sign_in.requestFocus()
            return true
        }
        if (password.isEmpty()) {
            editText_password_sign_in.error = "Enter password"
            editText_password_sign_in.requestFocus()
            return true
        }
        if (password.length < 6) {
            editText_password_sign_in.error = "Enter password more than 6 digits"
            editText_password_sign_in.requestFocus()
            return true
        }

        return false
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        btn_sign_in.isEnabled = editText_email_sign_in.text.trim().isNotBlank() &&
                editText_password_sign_in.text.trim().isNotBlank()

    }
}
