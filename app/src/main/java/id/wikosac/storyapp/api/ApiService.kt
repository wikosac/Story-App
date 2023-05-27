package id.wikosac.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun upload(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<ApiResponse>

    @GET("stories")
    fun storiesLocation(
        @Query("location") location: Int
    ): Call<StoryResponse>

    @GET("stories")
    suspend fun getStoryPaged(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse
}