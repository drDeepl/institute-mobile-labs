package com.labs.lab89

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.models.UserModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_log_in, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        usernameField = view.findViewById(R.id.username_ligin_edit_text)
        passwordField = view.findViewById(R.id.password_login_edit_text)
        val logInBtn: Button = view.findViewById(R.id.to_log_in_btn)
        dbHelper = DatabaseHelper(view.context)

        logInBtn.setOnClickListener {
            val username: String = usernameField.text.toString()
            val password: String = passwordField.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    view.context,
                    "есть незаполненные поля",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val user: UserModel? = dbHelper.getUserByUserName(username)
                if (user != null) {
                    clearFormLogIn()
                    saveUsernameInLS(view.context, username)
                    toUserInfoFragment(username)

                } else {
                    Toast.makeText(
                        view.context,
                        "неправильные имя пользователя или пароль",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun clearFormLogIn(){
        Log.i(TAG, "clearFormSignUp")
        usernameField.text.clear()
        passwordField.text.clear()
    }

    fun saveUsernameInLS(context: Context, username: String) {
        Log.i(TAG, "saveUsernameInLS")

        val sharedPreferences = context.getSharedPreferences("elib_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", username).apply()
    }

    private fun toUserInfoFragment(msg: String){
        Log.i(TAG, "toUserInfoFragment")
        val userInfoFragment: Fragment = UserInfoFragment().apply {
            arguments = Bundle().apply { putString("username", msg) }
        }

        this@LogInFragment.activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.user_account_fragment_container, userInfoFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}