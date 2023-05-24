package id.wikosac.storyapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import id.wikosac.storyapp.R
import id.wikosac.storyapp.databinding.ActivityRegisterBinding
import id.wikosac.storyapp.ui.custom.EmailEditText
import id.wikosac.storyapp.ui.custom.PassEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var edName: EditText
    private lateinit var edEmail: EmailEditText
    private lateinit var edPass: PassEditText
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edName = findViewById(R.id.ed_register_name)
        edEmail = findViewById(R.id.ed_register_email)
        edPass = findViewById(R.id.ed_register_password)

        binding.btnRegister.setOnClickListener {
            registerViewModel.register(edName.text.toString(), edEmail.text.toString(), edPass.text.toString())
            registerViewModel.message.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        registerViewModel.loginInfo.observe(this) {
            if (it.message == "User created") {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                edEmail.error = null
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val email = s.toString()
                if (!isEmailValid(email)) {
                    edEmail.error = "Invalid email address"
                } else {
                    edEmail.error = null
                }
                if (edPass.text.toString().isNotEmpty() && edPass.error == null) {
                    setMyButtonEnable()
                }
            }
        })

        edPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                edPass.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()
                if (!isPassValid(pass)) {
                    edPass.error = "Password must be greater than or equal to 8"
                } else {
                    edPass.error = null
                }
                if (edEmail.text.toString().isNotEmpty() && edEmail.error == null) {
                    setMyButtonEnable()
                }
            }
        })
    }

    private fun setMyButtonEnable() {
        binding.btnRegister.isEnabled = edEmail.error == null && edPass.error == null
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isPassValid(pass: String): Boolean {
        return pass.length >= 8
    }
}