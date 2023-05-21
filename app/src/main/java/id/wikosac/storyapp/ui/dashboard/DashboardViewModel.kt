package id.wikosac.storyapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.LoginResponse
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.api.StoryResponse
import id.wikosac.storyapp.ui.auth.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    private val _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList

    companion object{
        private const val TAG = "DashboardViewModel"
    }

    fun getStory(token: String) {
        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    _storyList.value = response.body()?.listStory
                    Log.d(TAG, "onResponse: ${_storyList.value}")
                } else {
                    Log.e(TAG, "onFailurei: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailuree: ${t.message.toString()}")
            }
        })
    }
}