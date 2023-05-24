package id.wikosac.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.R
import id.wikosac.storyapp.databinding.ActivityLoginBinding
import id.wikosac.storyapp.ui.custom.EmailEditText
import id.wikosac.storyapp.ui.custom.PassEditText
import id.wikosac.storyapp.ui.upload.UploadViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var edEmail: EmailEditText
    private lateinit var edPass: PassEditText
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val tokenPref = sharedPreferences.getString("TOKEN", "")
        if (tokenPref != "") {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        edEmail = findViewById(R.id.ed_login_email)
        edPass = findViewById(R.id.ed_login_password)

        binding.btnLogin.setOnClickListener {
            loginViewModel.login(edEmail.text.toString(), edPass.text.toString())
            loginViewModel.message.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        loginViewModel.loginInfo.observe(this) {
            Log.d("Login", "onCreate: ${loginViewModel.loginInfo.value}")
            if (it.message == "success") {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("token", it.loginResult.token)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                saveLoginSession(this, it.loginResult.token, it.loginResult.name)
            }
        }

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                edEmail.error = null
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

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

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

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
        binding.btnLogin.isEnabled = edEmail.error == null && edPass.error == null
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isPassValid(pass: String): Boolean {
        return pass.length >= 8
    }

    private fun saveLoginSession(context: Context, token: String, name: String) {
        val sharedPref = context.getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("TOKEN", token)
        editor.putString("NAME", name)
        editor.apply()
    }
}