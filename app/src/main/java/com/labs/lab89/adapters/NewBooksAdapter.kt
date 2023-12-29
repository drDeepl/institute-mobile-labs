package com.labs.lab89.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labs.lab89.database.DatabaseHelper
import com.labs.lab89.databinding.FragmentHomeBinding
import com.labs.lab89.databinding.HomeBookItemBinding
import com.labs.lab89.models.BookItemModel
import com.labs.lab89.models.BookModel

class NewBooksAdapter(
    val context: Context,
    val list: ArrayList<BookModel>
): RecyclerView.Adapter<NewBooksAdapter.NewViewHolder>() {

    private val TAG = this.javaClass.simpleName
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewBooksAdapter.NewViewHolder {
        val binding = HomeBookItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val listModel = list[position]
        holder.bookName.text =  listModel.name
        holder.bookAuthor.text = listModel.author
        holder.bookCountLike.text = listModel.countLikes.toString()
        holder.bookGenre.text = listModel.genre
        holder.bookLikeView.setOnClickListener{
            Log.i(TAG, "onClickLikeView")
            holder.bookCountLike.text = dbHelper.setLikeForBook(listModel.name).toString()
        }
    }

    class NewViewHolder(binding: HomeBookItemBinding): RecyclerView.ViewHolder(binding.root) {
        val bookName = binding.bookName
        val bookAuthor = binding.bookAuthor
        val bookCountLike = binding.firstCountLikes
        val bookGenre = binding.bookGenre
        val bookLikeView = binding.likeBook



    }
}