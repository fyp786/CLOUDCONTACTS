package com.codeseyee.cloydcontact


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codeseyee.cloydcontact.ViewModel.LoginViewModel
import com.codeseyee.cloydcontact.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupTxt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()

            viewModel.loginUser(email, password)
        }

        viewModel.loginResult.observe(this) { result ->
            val message = result.first
            val userId = result.second

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (message == "Login successful" && userId != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}
