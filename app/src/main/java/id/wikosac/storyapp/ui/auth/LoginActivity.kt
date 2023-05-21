package id.wikosac.storyapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
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
            Log.d("Login", "onCreate: ${loginViewModel.loginInfo}")
//            Toast.makeText(this@LoginActivity, email, Toast.LENGTH_SHORT).show()
        }

        setMyButtonEnable()
        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setMyButtonEnable() {
        val email = binding.edLoginEmail.text
        val pass = binding.edLoginPassword.text
        binding.btnLogin.isEnabled = (email != null && pass != null) && (email.toString().isNotEmpty() && email.toString().isNotEmpty())
    }
}