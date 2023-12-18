package com.example.lab5v2


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.lab5v2.api.ServiceBuilder
import com.example.lab5v2.api.interfaces.ApiInterface
import com.example.lab5v2.api.models.AccountModel
import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.MessageModel
import com.example.lab5v2.api.models.TokenModel
import com.example.lab5v2.service.TokenService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
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

        val showSignUpBtn: Button = findViewById(R.id.btn_to_show_sign_up_form)
        showSignUpBtn.setOnClickListener {
            setVisibleWelcomeCard(View.GONE)
            findViewById<TextView>(R.id.header_card_sign_in
            ).text = "регистрация"
            setVisibleLogInForm(View.VISIBLE)
            findViewById<Button>(R.id.btn_sign_up).visibility = View.VISIBLE
            findViewById<Button>(R.id.btn_log_in).visibility = View.GONE
            setVisibleWelcomeCard(View.GONE)
        }

        val showLogInBtn: Button = findViewById(R.id.btn_to_show_log_in_form)

        showLogInBtn.setOnClickListener {
            setVisibleLogInForm(View.VISIBLE)
            findViewById<Button>(R.id.btn_sign_up).visibility = View.GONE
            findViewById<Button>(R.id.btn_log_in).visibility = View.VISIBLE
            setVisibleWelcomeCard(View.GONE)


        }

        logInBtn.setOnClickListener {
            Log.i(TAG, "SET ON CLICK TO LOG IN BTN")
            logInBtn.visibility = View.VISIBLE
            logInBtn.visibility = View.GONE
            var siginIsSuccess = false
            var account: AccountModel? = null
            CoroutineScope(Dispatchers.Default).launch {
                val response: Response<TokenModel> =  requestToSigIn()
                Log.i(TAG, "${response.code()}")
                if(response.code() == 200){

                    tokenService.saveAccessToken(response.body()!!.token)
                    retrofit = ServiceBuilder.buildService(this@UserAccountActivity, ApiInterface::class.java)
                    val responseCurrentUserInfo: Response<AccountModel> = requestToCurrentUserInfo()
                    if(responseCurrentUserInfo.code() == 200){
                        findViewById<TextView>(R.id.header_card_sign_in).text = "Привет, ${responseCurrentUserInfo.body()?.username.toString()}!"
                        siginIsSuccess = true
                        account = responseCurrentUserInfo.body()
                    }
                }
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    logInBtn.visibility = View.VISIBLE
                    if(siginIsSuccess){
                        setVisibleLogInForm(View.GONE)
                        setVisibleWelcomeCard(View.GONE)
                        toShowAccountInfo(account)
                    }

                }
            }
        }
        val signUpBtn: Button = findViewById(R.id.btn_sign_up)
        signUpBtn.setOnClickListener {
            Log.i(TAG, "SET ON CLICK SIGN UP BTN")
            signUpBtn.visibility = View.GONE
            var signUpIsSuccess = false
            var account: AccountModel? = null
            CoroutineScope(Dispatchers.Default).launch {
                val response: Response<MessageModel> =  requestToSignUp()
                Log.i(TAG, "AFTER RESPONSE")
                Log.i(TAG, "${response.code()}\t ${response.message()}")
                if(response.code() == 200){

                    val responseSignIn: Response<TokenModel> =  requestToSigIn()
                    if(responseSignIn.code() == 200){
                        tokenService.saveAccessToken(responseSignIn.body()!!.token)
                        retrofit = ServiceBuilder.buildService(this@UserAccountActivity, ApiInterface::class.java)
                        val responseCurrentUserInfo: Response<AccountModel> = requestToCurrentUserInfo()
                        account = responseCurrentUserInfo.body()
                        if(responseCurrentUserInfo.code() == 200){
                            findViewById<TextView>(R.id.header_card_sign_in).text = "Привет, ${responseCurrentUserInfo.body()?.username.toString()}!"
                            signUpIsSuccess = true

                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    logInBtn.visibility = View.VISIBLE
                    if(signUpIsSuccess){
                        setVisibleLogInForm(View.GONE)
                        toShowAccountInfo(account)

                    }
                    else{
                        signUpBtn.visibility = View.VISIBLE
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

    private suspend fun requestToSigIn(): Response<TokenModel>{
        val accountSignInModel: AccountSignInModel = AccountSignInModel(passwordUserEditText.text.toString(), usernameEditText.text.toString())
        return retrofit.signin(accountSignInModel).execute()

    }

    private suspend fun requestToSignUp(): Response<MessageModel>{
        val accountSignInModel: AccountSignInModel = AccountSignInModel(passwordUserEditText.text.toString(), usernameEditText.text.toString())
        return retrofit.signup(accountSignInModel).execute()
    }

    private suspend fun requestToCurrentUserInfo(): Response<AccountModel>{
        return retrofit.currentUserInfo().execute()
    }

    private fun setVisibleLogInForm(visible: Int){
        findViewById<CardView>(R.id.login_in_form_card).visibility = visible
    }


    private fun setVisibleWelcomeCard(visible: Int){
        findViewById<CardView>(R.id.card_form_welcome).visibility = visible
    }

    private fun toShowAccountInfo(account: AccountModel?){
        Log.i(TAG, "TO SHOW ACCOUNT INFO")
        findViewById<CardView>(R.id.card_info_user).visibility = View.VISIBLE
        findViewById<TextView>(R.id.header_card_info_user).text = "Привет, ${account?.username}"
        findViewById<TextView>(R.id.card_balance_user).text = "Твой баланс: ${account?.balance.toString()}"
        val avatarView: AvatarView = findViewById(R.id.avatar_user)
        avatarView.loadImage(R.drawable.avatar_icon)
        // TODO: tets sliding panel

        val slideUpPanel: SlidingUpPanelLayout = findViewById(R.id.sliding_panel_account_info)
        slideUpPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        slideUpPanel.setFadeOnClickListener(View.OnClickListener {
//            slideUpPanel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED


        })


    }



}