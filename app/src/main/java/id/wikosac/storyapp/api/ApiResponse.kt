package id.wikosac.storyapp.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("token")
    val token: String
)

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("listStory")
    val listStory: List<Story>
)
