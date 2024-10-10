package com.naomi.myapplication.data

interface WordRepository {
    fun getRandomWord(): String
    fun getValidLetters(): List<String>
    fun getCorrectWords(): List<String>
}