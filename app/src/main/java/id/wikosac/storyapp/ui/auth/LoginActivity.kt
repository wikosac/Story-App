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

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(edEmail.text.toString(), edPass.text.toString())
            loginViewModel.message.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        loginViewModel.loginInfo.observe(this) {
            if (it.message == "success") {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                saveLoginSession(this, it.loginResult.token, it.loginResult.name)
            }
        }

        edEmail.setValidationListener(object : EmailEditText.ValidationListener {
            override fun onValidationSuccess() {
                edEmail.error = null
            }

            override fun onValidationFailure() {
                edEmail.error = "Invalid email address"
            }

        })

        edPass.setValidationListener(object : PassEditText.ValidationListener {
            override fun onValidationSuccess() {
                edPass.error = null
            }

            override fun onValidationFailure() {
                edPass.error = "Password must be greater than or equal to 8"
            }

        })

        edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableBtn()
            }

            override fun afterTextChanged(s: Editable?) {
                if (edPass.toString().isNotEmpty() && edPass.error == null) {
                    enableBtn()
                }
            }

        })

        edPass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableBtn()
            }

            override fun afterTextChanged(s: Editable?) {
                if (edEmail.toString().isNotEmpty() && edEmail.error == null) {
                    enableBtn()
                }
            }

        })
    }

    private fun enableBtn() {
        binding.btnLogin.isEnabled = (edEmail.toString().isNotEmpty() && edEmail.error == null) && (edPass.toString().isNotEmpty() && edPass.error == null)
    }

    private fun saveLoginSession(context: Context, token: String, name: String) {
        val sharedPref = context.getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("TOKEN", token)
        editor.putString("NAME", name)
        editor.apply()
    }
}