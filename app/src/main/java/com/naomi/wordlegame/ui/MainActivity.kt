

package com.naomi.wordlegame.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.naomi.myapplication.ui.viewmodel.GameViewModel
import com.naomi.wordlegame.R


class MainActivity : AppCompatActivity() {

    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var cellViews: List<TextView>
    private val maxAttempts = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGrid()
        setupKeyboard()

        gameViewModel.guesses.observe(this) { guesses ->
            if (guesses.size > gameViewModel.attempts.value!!) {
                updateGrid(guesses[gameViewModel.attempts.value!!])
            }
        }

        gameViewModel.attempts.observe(this) { attempts ->
            if (attempts >= maxAttempts) {
                showGameOverDialog("You've used all attempts. The correct word was ${gameViewModel.correctWord.value}.")
            }
        }
    }

    private fun setupGrid() {
        val gridLayout = findViewById<GridLayout>(R.id.wordleGrid)
        gridLayout.removeAllViews()

        cellViews = List(30) {
            TextView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 160
                    height = 190
                    setMargins(5, 5, 5, 5)
                }
                setBackgroundResource(R.drawable.word_cell_background)
                textSize = 24f
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
            }.also {
                gridLayout.addView(it)
            }
        }
    }

    private fun setupKeyboard() {
        val keyboardLayout = findViewById<LinearLayout>(R.id.keyboardLayout)
        val rows = arrayOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )

        for (row in rows) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            for (char in row) {
                val button = Button(this).apply {
                    layoutParams = LinearLayout.LayoutParams(90, 150).apply {
                        setMargins(5, 10, 5, 10)
                    }
                    text = char.toString()
                    textSize = 20f
                    setBackgroundResource(R.drawable.keyboard_button)
                    setOnClickListener { onKeyPress(char.toString()) }
                }
                rowLayout.addView(button)
            }
            keyboardLayout.addView(rowLayout)
        }


        val rowLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        val enterButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(160, 150).apply {
                setMargins(5, 10, 5, 10)
            }
            text = "Enter"
            textSize = 15f
            setBackgroundResource(R.drawable.keyboard_button)
            setOnClickListener { onEnterPress() }
        }
        rowLayout.addView(enterButton)

        val deleteButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(160, 150).apply {
                setMargins(5, 10, 5, 10)
            }
            setImageResource(R.drawable.ic_delete)
            setBackgroundResource(R.drawable.keyboard_button)
            setOnClickListener { onDeletePress() }
        }
        rowLayout.addView(deleteButton)
        keyboardLayout.addView(rowLayout)
    }

    private fun onKeyPress(key: String) {
        gameViewModel.onKeyPress(key)
    }

    private fun onDeletePress() {
        gameViewModel.onDeletePress()
    }

    private fun onEnterPress() {
        gameViewModel.onEnterPress()
        val currentGuess = gameViewModel.guesses.value?.get(gameViewModel.attempts.value!!) ?: return
        updateGrid(currentGuess)
    }

    private fun updateGrid(currentGuess: String) {
        val startIndex = gameViewModel.attempts.value!! * 5
        val correctWordValue = gameViewModel.correctWord.value ?: return

        for (i in currentGuess.indices) {
            val letter = currentGuess[i].toString()
            cellViews[startIndex + i].text = letter

            when {
                i < correctWordValue.length && letter == correctWordValue[i].toString() -> {
                    cellViews[startIndex + i].setBackgroundColor(Color.GREEN)
                }
                correctWordValue.contains(letter) -> {
                    cellViews[startIndex + i].setBackgroundColor(Color.YELLOW)
                }
                else -> {
                    cellViews[startIndex + i].setBackgroundColor(Color.GRAY)
                }
            }
        }

        for (i in currentGuess.length until 5) {
            cellViews[startIndex + i].text = ""
            cellViews[startIndex + i].setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun showGameOverDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Game Over")
            setMessage(message)
            setPositiveButton("OK") { _, _ -> gameViewModel.resetGame(); recreate() }
            show()
        }
    }
}
