package com.labs.lab89

import android.app.AlertDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginStart
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.exceptions.BookCreateModelHaveEmptyFields
import com.labs.lab89.models.BookCreateModel
import com.labs.lab89.models.BookModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout

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

        val addBookImg: ImageView = findViewById(R.id.add_book_image)
        addBookImg.setOnClickListener{
            showSlidingPanelAddBook()

        }
    }

    private fun createModelFromFormAddBook(): BookCreateModel{
        val name: String = findViewById<EditText>(R.id.name_book_create_field).text.toString()
        val description: String = findViewById<EditText>(R.id.description_book_create_field).toString()
        val genreId: Int = dbHelper.getGenreIdByName(spinnerGenres.selectedItem.toString())
        val author: String = findViewById<EditText>(R.id.author_book_create_field).text.toString()
        if(genreId < 0 || name.isEmpty() || description.isEmpty() || author.isEmpty()){
            throw BookCreateModelHaveEmptyFields("Некоторые поля пропущены")
        }
        Toast.makeText(this@MainActivity, "$name $description $genreId $author", Toast.LENGTH_SHORT).show()
        return BookCreateModel(name,description,genreId, author)
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

    private fun showSlidingPanelAddBook(){
        findViewById<SlidingUpPanelLayout>(R.id.sliding_layout).panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        var genres: List<String> = dbHelper.getGenres()
        if(genres.size == 0){
            genres += "выбор жанра"
        }
        genres += "добавить жанр"


        val adapterGenres: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item, genres)

        adapterGenres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenres.setAdapter(adapterGenres)

        spinnerGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("onItemSelected")
                val selectedItem: String = spinnerGenres.selectedItem.toString()
                if(selectedItem == "добавить жанр"){
                    Toast.makeText(this@MainActivity, "Добавить жанр", Toast.LENGTH_SHORT).show()
                    toShowDialogF4AddGenreAndAddUpdate2Adapter(adapterGenres)
                    spinnerGenres.clearFocus()

                }
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

    private fun toShowDialogF4AddGenreAndAddUpdate2Adapter(adapterGenres: ArrayAdapter<String>){
        val builder = AlertDialog.Builder(this)
        val nameGenre:EditText = EditText(this@MainActivity)
        nameGenre.marginStart
        nameGenre.hint = "жанр"
        builder.setTitle("добавление жанра")
        builder.setView(nameGenre)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if(!nameGenre.text.isEmpty()) {
                dbHelper.addToGenreTable(nameGenre.text.toString())
                adapterGenres.clear()
                adapterGenres.addAll(dbHelper.getGenres())
            }else{
                Toast.makeText(this@MainActivity, "Необходимо запонить поле", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }



    private fun clearFormAddBook(){
        findViewById<EditText>(R.id.name_book_create_field).text.clear()
        findViewById<EditText>(R.id.description_book_create_field).text.clear()
        findViewById<EditText>(R.id.author_book_create_field).text.clear()
    }

    override fun onDestroy() {
        database?.close()
        dbHelper?.close()
        super.onDestroy()
    }
}
