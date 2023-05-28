package id.wikosac.storyapp.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.api.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    private val _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getStoryLocation() {
        val client = ApiConfig.getApiService().storiesLocation(1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    _storyList.value = response.body()?.listStory
                    _message.value = response.body()?.message
                } else {
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _message.value = t.message.toString()
            }
        })
    }

    companion object{
        private const val TAG = "MapsViewModel"
    }
}