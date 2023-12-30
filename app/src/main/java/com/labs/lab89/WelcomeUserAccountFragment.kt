package com.labs.lab89

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class WelcomeUserAccountFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_welcome_user_account, container, false)
        val username: String? = getUsernameFromLS(view.context)
        if(username != null){
            toFragment(UserInfoFragment(), "username",username)

        }
        else{
            val showSignInForm: Button = view.findViewById(R.id.btn_to_show_sign_up_form)
            showSignInForm.setOnClickListener{
                Toast.makeText(it.context, "on Click show sign up form", Toast.LENGTH_LONG).show()
                toFragment(SignUpFragment(), null,null)
            }

            val showLogInForm: Button = view.findViewById(R.id.btn_to_show_log_in_form)
            showLogInForm.setOnClickListener{
                Toast.makeText(it.context, "on click show log in form", Toast.LENGTH_LONG).show()
                toFragment(LogInFragment(), null,null)
            }


        }

        return view
    }

    private fun toFragment(fragment: Fragment, key: String?, value: String?){
        fragment.apply {
            arguments = Bundle().apply { putString(key, value) }
        }
        this@WelcomeUserAccountFragment.activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.user_account_fragment_container, fragment)
            ?.commit()
    }

    fun getUsernameFromLS(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("elib_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomeUserAccount.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeUserAccountFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}