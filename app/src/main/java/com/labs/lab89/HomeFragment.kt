package com.labs.lab89

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.labs.lab89.adapters.NewBooksAdapter
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.models.BookItemModel
import com.labs.lab89.models.BookModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var newBooksAdapter: NewBooksAdapter
    private lateinit var listBooks: ArrayList<BookModel>
    private lateinit var  homeRV: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeRV = view.findViewById(R.id.home_RV)
        dbHelper = DatabaseHelper(view.context)
        listBooks = dbHelper.getBooks()


//        listBooks.add(BookModel("Мастер и Маргарита", "М.Булгаков", "классика",125))
//        listBooks.add(BookModel("Маленкьий принц", "А.Экзюпери", "сказка", 100))
//        listBooks.add(BookModel("Над пропастью во ржи", "А.Экзюпери", "роман", 110))
//        listBooks.add(BookModel("Демиан", "Неизвестно", "научная-фантастика", 150))

        newBooksAdapter = NewBooksAdapter(requireContext(), listBooks)
        homeRV.layoutManager = LinearLayoutManager(requireContext())
        homeRV.adapter = newBooksAdapter

        return view
    }
}