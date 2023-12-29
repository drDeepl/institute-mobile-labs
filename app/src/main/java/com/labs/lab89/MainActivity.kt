package com.labs.lab89

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.exceptions.BookCreateModelHaveEmptyFields
import com.labs.lab89.models.BookCreateModel
import com.labs.lab89.models.BookModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val TAG = this.javaClass.simpleName
    private lateinit var testGenre: TextView
    private lateinit var spinnerGenres: Spinner;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        val newBooksClickable: TextView = findViewById(R.id.new_books)
//        val popularBooksClickable:TextView = findViewById(R.id.popular_books)
//        popularBooksClickable.setOnClickListener{
//            Toast.makeText(this, "click to popular books", Toast.LENGTH_SHORT).show()
//        }

        newBooksClickable.setOnClickListener{
            Toast.makeText(this, "Click to new books", Toast.LENGTH_SHORT).show()
        }


        setHomeFragment()

        dbHelper.getGenres()




    }

    private fun setHomeFragment(){
        val homeFragment = HomeFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, homeFragment)
            .commit()

    }

    fun init(){
        testGenre = findViewById<TextView>(R.id.genre_text)
        val slidingPanel: SlidingUpPanelLayout = findViewById(R.id.sliding_layout)
        val dragView = findViewById<ImageView>(R.id.close_icon_sliding_panel)
        spinnerGenres = findViewById(R.id.genre_book_create_spinner)
        slidingPanel.setDragView(dragView)
//        dbHelper.addedInitGenres()
        setNavigation()
        toShowBoooks()
        val btnAddBook2Db: Button = findViewById(R.id.to_add_book_db)
        btnAddBook2Db.setOnClickListener{
            try{
                val modelFormAddBook: BookCreateModel = createModelFromFormAddBook()
                val isPutSuccess = dbHelper.addBookInTable(modelFormAddBook);
                if(isPutSuccess){
                    Toast.makeText(this, "Книга добавлена!", Toast.LENGTH_SHORT).show();
                    clearFormAddBook();
                    setHomeFragment()
                }
                else{
                    Toast.makeText(this, "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
                }
            }
            catch (eHaveEmptyFields: BookCreateModelHaveEmptyFields){
                Toast.makeText(this, "${eHaveEmptyFields.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun createModelFromFormAddBook(): BookCreateModel{
        val name: String = findViewById<EditText>(R.id.name_book_create_field).text.toString()
        val description: String = findViewById<EditText>(R.id.description_book_create_field).toString()
        val genreName: String = findViewById<Spinner>(R.id.genre_book_create_spinner).selectedItem.toString()
        val genreId: Int = dbHelper.getGenreIdByName(spinnerGenres.selectedItem.toString())
        val author: String = findViewById<EditText>(R.id.author_book_create_field).text.toString()
        if(genreId < 0 || name.isEmpty() || description.isEmpty() || author.isEmpty()){
            throw BookCreateModelHaveEmptyFields("Некоторые поля пропущены")
        }
        return BookCreateModel(name,description,genreId, author)
    }



    private fun setNavigation(){
        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.to_add_book -> {
                    showSlidingPanelAddBook()
//                    Toast.makeText(this, "click to add menu", Toast.LENGTH_SHORT).show()

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

    private fun showSlidingPanelAddBook(){
        findViewById<SlidingUpPanelLayout>(R.id.sliding_layout).panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        val genres: List<String> = dbHelper.getGenres()

        val adapterGenres: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item, genres)

        adapterGenres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenres.setAdapter(adapterGenres)

        spinnerGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("onItemSelected")
                val selectedItem: String = spinnerGenres.selectedItem.toString()
                Log.i(TAG, "ITEM SELECTED ${selectedItem}")
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i(TAG, "onNothingSelected")

            }
        }

    }

    private fun toShowBoooks(){
        val books: List<BookModel> = dbHelper.getBooks()

    }



    private fun clearFormAddBook(){
        findViewById<EditText>(R.id.name_book_create_field).text.clear()
        findViewById<EditText>(R.id.description_book_create_field).text.clear()
        findViewById<EditText>(R.id.author_book_create_field).text.clear()
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
