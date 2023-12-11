package com.example.lab5v2


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.lab5v2.api.ServiceBuilder
import com.example.lab5v2.api.interfaces.ApiInterface
import com.example.lab5v2.api.models.AccountModel
import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.TokenModel
import com.example.lab5v2.service.TokenService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserAccountActivity : AppCompatActivity() {
    private lateinit var passwordUserEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var logInBtn: Button
    private val TAG = this.javaClass.simpleName
    private lateinit var tokenService: TokenService;
    private  var accessToken: String? = null

    private lateinit var retrofit: ApiInterface


    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_account)
        init()
        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.to_profile-> toProfileActivity()
                R.id.to_main_activity -> toMainActivity()
            }
            true
        }

        logInBtn.setOnClickListener {
            logInBtn.visibility = View.VISIBLE
            logInBtn.visibility = View.GONE
            var siginIsSuccess = false
            CoroutineScope(Dispatchers.Default).launch {
                val response: Response<TokenModel> =  requestToSignIn()
                if(response.code() == 200){
                    tokenService.saveAccessToken(response.body()!!.token)
                    retrofit = ServiceBuilder.buildService(this@UserAccountActivity, ApiInterface::class.java)
                    val responseCurrentUserInfo: Response<AccountModel> = requestToCurrentUserInfo()
                    if(responseCurrentUserInfo.code() == 200){
                        findViewById<TextView>(R.id.header_card_sign_in).text = "Привет, ${responseCurrentUserInfo.body()?.username.toString()}!"
                        siginIsSuccess = true
                    }
                }
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    logInBtn.visibility = View.VISIBLE
                    if(siginIsSuccess){
                        toHideFormSignIn()
                    }


                }
            }
        }
    }

    private fun init(){
        passwordUserEditText = findViewById(R.id.password_edit_text)
        usernameEditText = findViewById(R.id.username_edit_text)
        logInBtn = findViewById(R.id.btn_log_in)
        tokenService = TokenService(this)
        retrofit = ServiceBuilder.buildService(this, ApiInterface::class.java)
        accessToken = tokenService.getAccessToken()
        progressBar = findViewById(R.id.progressBar)

    }

    private fun toProfileActivity(){
        val intent: Intent = Intent(this, UserAccountActivity::class.java)
        startActivity(intent)

    }
    private fun toMainActivity(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


    private fun hideSignInForm(){
        findViewById<CardView>(R.id.sign_in_form_card).visibility = View.GONE
    }

    private suspend fun requestToSignIn(): Response<TokenModel>{
        val accountSignInModel: AccountSignInModel = AccountSignInModel(passwordUserEditText.text.toString(), usernameEditText.text.toString())
        return retrofit.signin(accountSignInModel).execute()
    }

    private suspend fun requestToCurrentUserInfo(): Response<AccountModel>{
        return retrofit.currentUserInfo().execute()
    }

    private fun toHideFormSignIn(){
        usernameEditText.visibility = View.GONE
        passwordUserEditText.visibility = View.GONE
        logInBtn.visibility = View.GONE

    }

    private fun toShowAccountInfo(account: AccountModel){
//        TODO: dynamically added text view
    }



}