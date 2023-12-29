package com.labs.lab89.models

class BookItemModel {

    private lateinit var nameBook:String
    private lateinit var author: String
    private  var countLikes: Int = 0
    constructor()
    constructor(nameBook: String, author: String, countLikes: Int){
        this.nameBook = nameBook
        this.author = author
        this.countLikes = countLikes
    }

    fun getNameBook(): String {
        return nameBook
    }

    fun getAuthor(): String{
        return author
    }

    fun getCountLikes(): Int{
        return countLikes
    }
}