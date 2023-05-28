package id.wikosac.storyapp.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.data.Injection
import id.wikosac.storyapp.data.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun storyPaged(): LiveData<PagingData<Story>> = storyRepository.getPagedStory().cachedIn(viewModelScope)

}

class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.setRepo()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}