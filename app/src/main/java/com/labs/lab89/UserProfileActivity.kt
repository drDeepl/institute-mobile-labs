package com.labs.lab89

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.labs.lab89.exceptions.BookCreateModelHaveEmptyFields
import com.labs.lab89.models.BookCreateModel

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        init()
    }

    private fun init(){
        toUserWelcomeFragment()
        setNavigation()

    }

    private fun toUserWelcomeFragment(){
        val userAccountWelcomeFragment: WelcomeUserAccountFragment = WelcomeUserAccountFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.user_account_fragment_container, userAccountWelcomeFragment)
            .commit()
    }






    private fun setNavigation(){
        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.to_user_profile_activity-> {
                    val intent: Intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.to_main_activity -> {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

}