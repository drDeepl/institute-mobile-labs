package com.example.lab5v2


import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import com.example.lab5v2.api.ServiceBuilder
import com.example.lab5v2.api.interfaces.ApiInterface
import com.example.lab5v2.api.models.Account
import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.TokenModel
import com.example.lab5v2.service.TokenService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            CoroutineScope(Dispatchers.Default).launch {
                val response: Response<TokenModel> =  requestToSignIn()
                if(response.code() == 200){
                    tokenService.saveAccessToken(response.body()!!.token)
                    val response: Response<Account> = retrofit.me().execute()
                    findViewById<TextView>(R.id.header_card_sign_in).text = response.code().toString()
                    hideSignInForm()

                }
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    logInBtn.visibility = View.VISIBLE
                }
            }

//            Toast.makeText(this@UserAccountActivity, "${response.code()}", Toast.LENGTH_LONG ).show()
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

    private fun toHideFormSignIn(){
        val cardSignIn: CardView = findViewById(R.id.sign_in_form_card)
        cardSignIn.visibility = View.GONE

    }

    private fun toShowAccountInfo(account: Account){
//        TODO: dynamically added text view
    }



}