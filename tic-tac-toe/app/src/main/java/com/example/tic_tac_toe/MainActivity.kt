package com.example.tic_tac_toe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.example.tic_tac_toe.databinding.MainBinding

class MainActivity : ComponentActivity() {

    private var firstTurn = CROSS
    private var currentTurn = CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    private fun isBoardFull(): Boolean {
        for (button in boardList) {
            if (button.text == "") {
                return false
            }
        }

        return true
    }

    private fun showGameResult(title: String) {
        AlertDialog.Builder(this).setTitle(title)
        .setMessage("X ניצח $crossesScore פעמים\nO ניצח $noughtsScore פעמים")
        .setPositiveButton("התחל מחדש") { _, _ ->
            resetBoard()
        }.setCancelable(false).show()

    }

    private fun resetBoard() {
        for (button in boardList) {
            button.text = ""
        }

        val newFirstTurn = if (firstTurn == NOUGHT) CROSS else NOUGHT
        firstTurn = newFirstTurn
        currentTurn = newFirstTurn
        setTurnLabel()
    }

    fun boardTapped(view: View) {
        if (view !is Button)
            return

        addToBoard(view)

        val winner = getWinner()

        if (winner == CROSS) crossesScore++
        if (winner == NOUGHT) noughtsScore++

        if (winner != "") {
            showGameResult("$winner ניצח!")
        }

        if (isBoardFull()) {
            showGameResult("תיקו")
        }
    }

    private fun getWinner(): String {
        val firstRow: Array<Int> = arrayOf(0, 1, 2)
        val secondRow: Array<Int> = arrayOf(3, 4, 5)
        val thirdRow: Array<Int> = arrayOf(6, 7, 8)
        val firstColumn: Array<Int> = arrayOf(0, 3, 6)
        val secondColumn: Array<Int> = arrayOf(1, 4, 7)
        val thirdColumn: Array<Int> = arrayOf(2, 5, 8)
        val topLeftDiagonal: Array<Int> = arrayOf(0, 4, 8)
        val topRightDiagonal: Array<Int> = arrayOf(2, 4, 6)

        val winningRoutesIndexes: Array<Array<Int>> = arrayOf(
            firstRow,
            secondRow,
            thirdRow,
            firstColumn,
            secondColumn,
            thirdColumn,
            topLeftDiagonal,
            topRightDiagonal
        )

        for (routeIndexes in winningRoutesIndexes) {
            if (findButtonTextByIndex(routeIndexes[0]) != "" &&
                findButtonTextByIndex(routeIndexes[0]) == findButtonTextByIndex(routeIndexes[1]) &&
                findButtonTextByIndex(routeIndexes[1]) == findButtonTextByIndex(routeIndexes[2])) {
                return findButtonTextByIndex(routeIndexes[0])
            }
        }

        return ""
    }

    private fun findButtonTextByIndex(index: Int):String = boardList[index].text.toString()

    private fun addToBoard(button: Button) {
        if (button.text != "")
            return

        if (currentTurn == NOUGHT) {
            button.text = NOUGHT
            currentTurn = CROSS
        } else if (currentTurn == CROSS) {
            button.text = CROSS
            currentTurn = NOUGHT
        }

        setTurnLabel()
    }

    private fun setTurnLabel() {
        val turnText = "תור $currentTurn"

        binding.turnTV.text = turnText
    }

    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
}
