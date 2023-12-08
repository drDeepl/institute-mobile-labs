package com.example.lab5v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lab5v2.api.ServiceBuilder
import com.example.lab5v2.api.interfaces.ApiInterface
import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.TokenModel
import com.example.lab5v2.service.TokenService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAccountActivity : AppCompatActivity() {
    private lateinit var passwordUserEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var logInBtn: Button
    private val TAG = this.javaClass.simpleName
    private lateinit var tokenService: TokenService;
    private  var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)
        init()
        logInBtn.setOnClickListener {
            val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
            val accountSignInModel: AccountSignInModel = AccountSignInModel(passwordUserEditText.text.toString(), usernameEditText.text.toString())

            retrofit.signin(accountSignInModel).enqueue(
                object: Callback<TokenModel> {
                    override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {



                        val token: String = response.body()?.token.toString()
                        tokenService.saveAccessToken(token)
                        Toast.makeText(this@UserAccountActivity, "ТОКЕН СОХРАНЕН", Toast.LENGTH_LONG ).show()

                    }

                    override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                        Toast.makeText(this@UserAccountActivity, "Ошибка при входе", Toast.LENGTH_LONG ).show()
                        Log.e(TAG, t.message.toString())

                    }


                }
            )

        }


    }

    private fun init(){
        passwordUserEditText = findViewById(R.id.password_edit_text)
        usernameEditText = findViewById(R.id.username_edit_text)
        logInBtn = findViewById(R.id.btn_log_in)
        tokenService = TokenService(this)
        accessToken = tokenService.getAccessToken()


    }

}