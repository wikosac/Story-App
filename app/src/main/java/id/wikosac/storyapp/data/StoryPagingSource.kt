package id.wikosac.storyapp.data

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.PagingSource
import id.wikosac.storyapp.api.ApiService
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.api.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoryPaged(page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e("storyzz", "loadee: $exception")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
