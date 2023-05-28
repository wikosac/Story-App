package id.wikosac.storyapp.utils

import id.wikosac.storyapp.api.Story

object DataDummy {
    fun storyDummy(): List<Story> {
        val listOfStories = ArrayList<Story>()
        for (n in 0..15) {
            val stories = Story(
                "$n",
                "url $n",
                "time $n",
                "name $n",
                "desc $n"
            )
            listOfStories.add(stories)
        }
        return listOfStories
    }
}