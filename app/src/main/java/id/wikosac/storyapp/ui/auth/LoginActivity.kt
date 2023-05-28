package id.wikosac.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.databinding.ActivityLoginBinding
import id.wikosac.storyapp.ui.custom.EmailEditText
import id.wikosac.storyapp.ui.custom.PassEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var edEmail: EmailEditText
    private lateinit var edPass: PassEditText
    private lateinit var sharedPref: SharedPreferences
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        TOKEN_PREF = sharedPref.getString(TOKEN, "").toString()
        NAME_PREF = sharedPref.getString(NAME, "").toString()
        if (TOKEN_PREF != "") {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        edEmail = binding.edLoginEmail
        edPass = binding.edLoginPassword

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
                saveLoginSession(this, it.loginResult.token, it.loginResult.name)
                TOKEN_PREF = sharedPref.getString(TOKEN, "").toString()
                NAME_PREF = sharedPref.getString(NAME, "").toString()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
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
        val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(TOKEN, token)
        editor.putString(NAME, name)
        editor.apply()
    }

    companion object {
        const val SESSION = "session"
        const val TOKEN = "token"
        const val NAME = "name"
        var TOKEN_PREF = ""
        var NAME_PREF = ""
    }
}