package id.wikosac.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    private val _loginInfo = MutableLiveData<ApiResponse>()
    val loginInfo: LiveData<ApiResponse> = _loginInfo
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun register(userName: String, userEmail: String, userPass: String) {
        val client = ApiConfig.getApiService().register(userName, userEmail, userPass)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    _loginInfo.value = response.body()
                    _message.value = _loginInfo.value?.message.toString()
                } else {
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _message.value = t.message.toString()
            }
        })
    }
}