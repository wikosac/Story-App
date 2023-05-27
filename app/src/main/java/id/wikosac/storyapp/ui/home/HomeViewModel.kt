package id.wikosac.storyapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.myunlimitedquotes.di.Injection
import id.wikosac.storyapp.api.ApiConfig
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.api.StoryResponse
import id.wikosac.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)
    private val _storyList = MutableLiveData<List<Story>>()
    val storyList: LiveData<List<Story>> = _storyList

    fun getStory() {
        val client = ApiConfig.getApiService().getStory(1, 5)
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

    companion object{
        private const val TAG = "HomeViewModel"
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}