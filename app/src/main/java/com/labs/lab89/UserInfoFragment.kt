package com.labs.lab89

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_user_info, container, false)
        val username: String? = arguments?.getString("username")
        val welcomeText: TextView = view.findViewById<TextView>(R.id.welcome_userinfo_text)
        welcomeText.text = "${welcomeText.text} $username"
        val toExitImgClickable: ImageView = view.findViewById(R.id.to_exit_image_view)
        toExitImgClickable.setOnClickListener{
            removeUsernameFromLS(it.context)
            toUserWelcomeFragment()
        }
        return view
    }

    private fun removeUsernameFromLS(context: Context){
        context.getSharedPreferences("elib_prefs", Context.MODE_PRIVATE).edit().remove("username").apply()
    }

    private fun toUserWelcomeFragment(){
        val userAccountWelcomeFragment: WelcomeUserAccountFragment = WelcomeUserAccountFragment()
        this@UserInfoFragment.activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.user_account_fragment_container, userAccountWelcomeFragment)
            ?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}