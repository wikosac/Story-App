package id.wikosac.storyapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.R
import id.wikosac.storyapp.databinding.ActivityLoginBinding
import id.wikosac.storyapp.ui.custom.EmailEditText
import id.wikosac.storyapp.ui.custom.PassEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var edEmail: EmailEditText
    private lateinit var edPass: PassEditText
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edEmail = findViewById(R.id.ed_login_email)
        edPass = findViewById(R.id.ed_login_password)
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(edEmail.text.toString(), edPass.text.toString())
//            Toast.makeText(this@LoginActivity, email, Toast.LENGTH_SHORT).show()
        }
        loginViewModel.loginInfo.observe(this) {
            Log.d("Login", "onCreate: ${loginViewModel.loginInfo.value}")
            if (it.message == "success") {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("token", it.loginResult.token)
                startActivity(intent)
            }
        }

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val email = s.toString()
                if (!isEmailValid(email)) {
                    edEmail.error = "Invalid email address"
                } else {
                    edEmail.error = null
                }
            }
        })

        edPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()
                if (!isPassValid(pass)) {
                    edPass.error = "Password must be greater than or equal to 8"
                } else {
                    edPass.error = null
                    setMyButtonEnable()
                }
            }
        })
    }

    private fun setMyButtonEnable() {
        binding.btnLogin.isEnabled = edPass.text.toString().isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isPassValid(pass: String): Boolean {
        return pass.length >= 8
    }

}