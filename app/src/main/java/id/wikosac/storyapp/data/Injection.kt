package id.wikosac.storyapp.data

import id.wikosac.storyapp.api.ApiConfig

object Injection {
    fun setRepo(): StoryRepository {
        val service = ApiConfig.getApiService()
        return StoryRepository(service)
    }
}