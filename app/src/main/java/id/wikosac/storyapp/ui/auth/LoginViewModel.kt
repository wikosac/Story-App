package id.wikosac.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.UserInfo
import id.wikosac.storyapp.ui.custom.PassEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _loginInfo = MutableLiveData<UserInfo>()
    val loginInfo: LiveData<UserInfo> = _loginInfo

    companion object{
        private const val TAG = "LoginViewModel"
    }

    fun login(userEmail: String, userPass: String) {
        val client = ApiConfig.getApiService().login(userEmail, userPass)
        client.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    _loginInfo.value = response.body()
                    Log.d(TAG, "onResponse: $_loginInfo")
                } else {
                    Log.e(TAG, "onFailurei: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.e(TAG, "onFailuree: ${t.message.toString()}")
            }
        })
    }
}