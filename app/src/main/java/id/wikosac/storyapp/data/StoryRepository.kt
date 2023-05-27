package id.wikosac.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.myunlimitedquotes.database.StoryDatabase
import id.wikosac.storyapp.api.ApiService
import id.wikosac.storyapp.api.Story

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
    ) {

    fun getPagedStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}