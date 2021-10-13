package com.example.rssfeedpractice

data class Question(val id: String?, val title: String?){
    override fun toString(): String = title!!
}