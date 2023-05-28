package id.wikosac.storyapp.data

import androidx.paging.PagingState
import androidx.paging.PagingSource
import id.wikosac.storyapp.api.ApiService
import id.wikosac.storyapp.api.Story

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1
            val service = apiService.getStoryPaged(page, params.loadSize)

            LoadResult.Page(
                data = service.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (service.listStory.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
