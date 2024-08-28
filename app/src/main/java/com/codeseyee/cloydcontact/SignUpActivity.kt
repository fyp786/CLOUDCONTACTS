package com.codeseyee.cloydcontact


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codeseyee.cloydcontact.ViewModel.SignUpViewModel
import com.codeseyee.cloydcontact.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private var profileImageUri: String? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.profileImage.setImageURI(it)
            profileImageUri = it.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.signup.setOnClickListener {
            val email = binding.email.editText?.text.toString()
            val phone = binding.phone.editText?.text.toString()
            val name = binding.name.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val confirmPassword = binding.confirmPassword.editText?.text.toString()
            val accountType = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.radioIndividual -> "Individual"
                R.id.radioBusiness -> "Business"
                else -> ""
            }

            // Validate fields
            if (email.isEmpty() || phone.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || accountType.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signUpUser(email, phone, name, password, profileImageUri, accountType)
        }

        viewModel.signupResult.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            if (result == "Signup successful") {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}