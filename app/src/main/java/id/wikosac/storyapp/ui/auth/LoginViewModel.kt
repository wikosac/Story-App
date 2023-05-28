package id.wikosac.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _loginInfo = MutableLiveData<LoginResponse>()
    val loginInfo: LiveData<LoginResponse> = _loginInfo
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun login(userEmail: String, userPass: String) {
        val client = ApiConfig.getApiService().login(userEmail, userPass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginInfo.value = response.body()
                    _message.value = _loginInfo.value?.message.toString()
                } else {
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _message.value = t.message.toString()
            }
        })
    }
}