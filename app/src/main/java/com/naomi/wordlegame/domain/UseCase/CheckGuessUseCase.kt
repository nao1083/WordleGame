package com.naomi.myapplication.domain.UseCase

import android.graphics.Color
import com.naomi.myapplication.data.WordRepository


class CheckGuessUseCase(private val wordRepository: WordRepository) {

    fun execute(guess: String, correctWord: String): List<Int> {
        val resultColors = IntArray(guess.length) { Color.GRAY }


        val correctWordFrequency = mutableMapOf<Char, Int>()
        for (char in correctWord) {
            correctWordFrequency[char] = correctWordFrequency.getOrDefault(char, 0) + 1
        }

        for (i in guess.indices) {
            if (guess[i] == correctWord[i]) {
                resultColors[i] = Color.GREEN
                correctWordFrequency[guess[i]] = correctWordFrequency.getValue(guess[i]) - 1
            }
        }

        for (i in guess.indices) {
            if (resultColors[i] == Color.GRAY && correctWordFrequency.containsKey(guess[i]) &&
                correctWordFrequency[guess[i]]!! > 0) {
                resultColors[i] = Color.YELLOW
                correctWordFrequency[guess[i]] = correctWordFrequency.getValue(guess[i]) - 1
            }
        }

        return resultColors.toList()
    }
}
