package com.naomi.myapplication.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val correctWords = listOf("PLANT", "AGUDO", "ALTAR", "AGUAS", "AZUL","MAMAS", "PAPAS", "TODOS", "ARBOL", "KEVIN")
    private val _correctWord = MutableLiveData<String>()
    val correctWord: LiveData<String> get() = _correctWord

    init {
        selectRandomWord()
    }

    private fun selectRandomWord() {
        _correctWord.value = correctWords[Random.nextInt(correctWords.size)]
    }


    private val _attempts = MutableLiveData(0)
    val attempts: LiveData<Int> get() = _attempts

    private val _guesses = MutableLiveData<MutableList<String>>(MutableList(6) { "" }) // Inicializa con 6 intentos vacíos
    val guesses: LiveData<MutableList<String>> get() = _guesses

    private val _colors = MutableLiveData<MutableList<List<String>>>(MutableList(6) { List(5) { "" } }) // Para almacenar los colores
    val colors: LiveData<MutableList<List<String>>> get() = _colors

    fun onKeyPress(key: String) {
        val currentAttempts = _attempts.value ?: 0
        val currentGuesses = _guesses.value ?: mutableListOf()

        if (currentGuesses.size > currentAttempts && currentGuesses[currentAttempts].length < 5) {
            var currentGuess = currentGuesses[currentAttempts]
            currentGuess += key

            currentGuesses[currentAttempts] = currentGuess
            _guesses.value = currentGuesses
        }
    }

    fun onDeletePress() {
        val currentAttempts = _attempts.value ?: 0
        val currentGuesses = _guesses.value ?: mutableListOf()

        if (currentGuesses.size > currentAttempts) {
            val currentGuess = currentGuesses[currentAttempts]
            if (currentGuess.isNotEmpty()) {
                currentGuesses[currentAttempts] = currentGuess.dropLast(1)
                _guesses.value = currentGuesses
            }
        }
    }

    fun onEnterPress() {
        if (_guesses.value?.size ?: 0 > _attempts.value!!) {
            val currentAttempt = _attempts.value ?: 0
            val currentGuess = _guesses.value?.get(currentAttempt) ?: ""

            val colorsList = List(5) { "" }.toMutableList()
            val correctWordValue = _correctWord.value ?: ""

            for (i in 0 until 5) {
                if (currentGuess[i].uppercase() == correctWordValue[i].uppercase()) {
                    colorsList[i] = "green"
                }
            }

            for (i in 0 until 5) {
                if (colorsList[i] != "green" && currentGuess[i].uppercase() in correctWordValue) {
                    colorsList[i] = "yellow" // Color amarillo para letras en la palabra pero en posición incorrecta
                }
            }

            for (i in 0 until 5) {
                if (colorsList[i] == "" && currentGuess[i].uppercase() !in correctWordValue) {
                    colorsList[i] = "gray"
                }
            }

            val currentColors = _colors.value ?: MutableList(6) { List(5) { "" } }
            currentColors[currentAttempt] = colorsList
            _colors.value = currentColors

            _attempts.value = currentAttempt + 1
        }
    }

    fun resetGame() {
        _attempts.value = 0
        _guesses.value = MutableList(6) { "" }
        _colors.value = MutableList(6) { List(5) { "" } }
    }
}
