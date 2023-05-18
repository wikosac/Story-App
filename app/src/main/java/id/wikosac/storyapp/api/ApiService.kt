package id.wikosac.storyapp.api

import com.google.gson.annotations.SerializedName
import id.wikosac.storyapp.api.LoginResult
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

data class Response(
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

interface ApiService {
    @Multipart
    @POST("/stories/guest")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<Response>

    @POST("/register")
    fun register(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<Response>

    @POST("/login")
    fun login(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}