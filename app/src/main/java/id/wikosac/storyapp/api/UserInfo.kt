package id.wikosac.storyapp.api

import android.text.Editable
import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("user_email")
    val userEmail: Editable?,

    @SerializedName("user_password")
    val userPassword: Editable?
)
