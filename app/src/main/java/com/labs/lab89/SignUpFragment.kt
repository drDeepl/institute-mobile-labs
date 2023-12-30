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
import android.widget.Toast
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.models.UserModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private  lateinit var usernameField:EditText
    private lateinit var passwordField: EditText
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        init(view)
        // Inflate the layout for this fragment
        return view
    }

    private fun init(view: View){
        usernameField = view.findViewById(R.id.username_signup_edit_text)
        passwordField = view.findViewById(R.id.password_signup_edit_text)
        val signUpBtn: Button = view.findViewById(R.id.to_sign_up_btn)
        dbHelper = DatabaseHelper(view.context)

        signUpBtn.setOnClickListener {
            val username: String = usernameField.text.toString()
            val password: String = passwordField.text.toString()
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(view.context, "есть незаполненные поля: $username $password", Toast.LENGTH_LONG).show()
            }
            else{
                val user: UserModel? = dbHelper.getUserByUserName(username)
                if(user != null){
                    Toast.makeText(view.context, "Такой пользователь уже существует", Toast.LENGTH_LONG).show()
                }
                else{
                    val userModel = UserModel(username, password, false)
                    if(dbHelper.addUser(userModel)){
                        clearFormSignUp()
                        saveUsernameInLS(view.context, username)
                        toUserInfoFragment(username)
                    }
                    else{
                        Toast.makeText(view.context, "Ошибка регистрации", Toast.LENGTH_LONG).show()

                    }



                }
            }
        }

    }

    private fun clearFormSignUp(){
        Log.i(TAG, "clearFormSignUp")
        usernameField.text.clear()
        passwordField.text.clear()
    }

    fun saveUsernameInLS(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences("elib_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", username).apply()
    }

    private fun toUserInfoFragment(msg: String){
        Log.i(TAG, "toUserInfoFragment")
        val userInfoFragment: Fragment = UserInfoFragment().apply {
            arguments = Bundle().apply { putString("username", msg) }
        }

        this@SignUpFragment.activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.user_account_fragment_container, userInfoFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}