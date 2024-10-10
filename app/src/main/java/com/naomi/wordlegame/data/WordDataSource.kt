package com.naomi.myapplication.data
class WordDataSource {

    private val correctWords = listOf("PLANT", "AGUDO", "ALTAR", "AGUAS", "AZUL","MAMAS", "PAPAS", "TODOS", "ARBOL", "KEVIN")
    private val validLetters = listOf(
        "A", "B", "C", "D", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
        "V", "W", "X", "Y", "Z"
    )

    fun getRandomWord(): String {
        return correctWords.random()
    }

    fun getValidWords(): List<String> {
        return correctWords + validLetters
    }

    fun getValidLetters(): List<String> {
        return validLetters
    }
    fun getCorrectWords(): List<String> {
        return correctWords
    }

}
