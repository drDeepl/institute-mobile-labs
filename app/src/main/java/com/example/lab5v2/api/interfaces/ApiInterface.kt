package com.example.lab5v2.api.interfaces

import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.TokenModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("account/signin")
    fun signin(@Body requestModel: AccountSignInModel): Call<TokenModel>

}