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

    val story: LiveData<PagingData<Story>> = storyRepository.getPagedStory().cachedIn(viewModelScope)

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