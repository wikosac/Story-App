package id.wikosac.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(
        @Body userData: UserInfo
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
        ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}