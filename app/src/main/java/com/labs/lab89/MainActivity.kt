package com.labs.lab89

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.models.BookCreateModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val TAG = this.javaClass.simpleName
    private lateinit var testGenre: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        val newBooksClickable: TextView = findViewById(R.id.new_books)
        val popularBooksClickable:TextView = findViewById(R.id.popular_books)

        newBooksClickable.setOnClickListener{
            Toast.makeText(this, "Click to new books", Toast.LENGTH_SHORT).show()
        }

        popularBooksClickable.setOnClickListener{
            Toast.makeText(this, "click to popular books", Toast.LENGTH_SHORT).show()
        }


//        dbHelper = DatabaseHelper(this)
//        database = dbHelper?.writableDatabase


//        dbHelper.addToGenreTable("genre", "фэнтези")
        dbHelper.getGenres()

    }

    fun init(){
        testGenre = findViewById<TextView>(R.id.genre_text)
        val slidingPanel: SlidingUpPanelLayout = findViewById(R.id.sliding_layout)
        val dragView = findViewById<ImageView>(R.id.close_icon_sliding_panel)
        slidingPanel.setDragView(dragView)
        setNavigation()
        val btnAddBook2Db: Button = findViewById(R.id.to_add_book_db)
        btnAddBook2Db.setOnClickListener{
            createModelFromFormAddBook()
        }
    }

    private fun createModelFromFormAddBook(){
        val name: String = findViewById<EditText>(R.id.name_book_create_field).text.toString()
        val description: String = findViewById<EditText>(R.id.description_book_create_field).toString()
        val genreName: String = findViewById<Spinner>(R.id.genre_book_create_spinner).selectedItem.toString()
        val author: String = findViewById<EditText>(R.id.author_book_create_field).toString()
//        return BookCreateModel(name,description,genreId, author)
    }

    private fun generateButtons(btns: Array<String>){
        val distanceInDp = 20
        val layout: ConstraintLayout = findViewById(R.id.main_layout)
        btns.forEachIndexed{ index, buttonText ->
            val btn: Button = Button(this)
            btn.text = buttonText

            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

//            params.setMargins(0, getPxFromDp(distanceInDp) * index, 0, 0)

        }
    }

    private fun setNavigation(){
        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.to_add_book -> {
                    changeStateSldiingPanel()
                    Toast.makeText(this, "click to add menu", Toast.LENGTH_SHORT).show()

                }
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

    private fun changeStateSldiingPanel(){
        findViewById<SlidingUpPanelLayout>(R.id.sliding_layout).panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }



    private fun getPxFromDp(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onDestroy() {
        database?.close()
        dbHelper?.close()
        super.onDestroy()
    }
}
