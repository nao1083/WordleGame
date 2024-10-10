package com.naomi.myapplication.infrastructure.repositories

import com.naomi.myapplication.data.WordDataSource
import com.naomi.myapplication.data.WordRepository

class WordRepositoryImpl(private val wordDataSource: WordDataSource) : WordRepository {

    override fun getRandomWord(): String {
        return wordDataSource.getRandomWord()
    }
    override fun getValidLetters(): List<String> {
        return wordDataSource.getValidLetters()
    }

    override fun getCorrectWords(): List<String> { // Implementación del nuevo método
        return wordDataSource.getCorrectWords()
    }

}