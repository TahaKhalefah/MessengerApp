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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tahadroid.messenger.model.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), TextWatcher {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firstoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = firstoreInstance.document("users/${auth.currentUser?.uid.toString()}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firstoreInstance.collection("users").document(auth.currentUser?.uid.toString())

        editText_email_sign_up.addTextChangedListener(this@SignUpActivity)
        editText_name_sign_up.addTextChangedListener(this@SignUpActivity)
        editText_password_sign_up.addTextChangedListener(this@SignUpActivity)

        btn_sign_up.setOnClickListener {
            val name = editText_name_sign_up.text.toString().trim()
            val email = editText_email_sign_up.text.toString().trim()
            val password = editText_password_sign_up.text.toString().trim()

            if (checkValidate(name, email, password)) return@setOnClickListener
            progressBar_sign_up.visibility= View.VISIBLE
            createNewAccount(name,email, password)
        }
    }

    private fun checkValidate(
        name: String,
        email: String,
        password: String
    ): Boolean {
        if (name.isEmpty()) {
            editText_name_sign_up.error = "Enter name"
            editText_name_sign_up.requestFocus()
            return true
        }
        if (email.isEmpty()) {
            editText_email_sign_up.error = "Enter email"
            editText_email_sign_up.requestFocus()
            return true
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_email_sign_up.error = "Invalid email"
            editText_email_sign_up.requestFocus()
            return true
        }
        if (password.isEmpty()) {
            editText_password_sign_up.error = "Enter password"
            editText_password_sign_up.requestFocus()
            return true
        }
        if (password.length < 6) {
            editText_password_sign_up.error = "Enter password more than 6 digits"
            editText_password_sign_up.requestFocus()
            return true
        }

        return false
    }

    private fun createNewAccount(name:String,email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val newUser= User(name)
            currentUserDocRef.set(newUser)
            if (task.isSuccessful) {
                progressBar_sign_up.visibility= View.GONE
                val intentMianActivity = Intent(this@SignUpActivity, MainActivity::class.java)
                intentMianActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentMianActivity)
            } else {
                progressBar_sign_up.visibility= View.GONE
                Toast.makeText(this@SignUpActivity, task.exception?.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        btn_sign_up.isEnabled = editText_email_sign_up.text.trim().isNotBlank() &&
                editText_name_sign_up.text.trim().isNotBlank() &&
                editText_password_sign_up.text.trim().isNotBlank()
    }
}
