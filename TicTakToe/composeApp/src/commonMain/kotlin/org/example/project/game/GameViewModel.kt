package org.example.project.game
import androidx.compose.runtime.*
import kotlin.random.Random

class GameViewModel(private val engine: GameEngine) {
    var board by mutableStateOf(engine.emptyBoard())
    var currentPlayer by mutableStateOf(Cell.X)
    var winResult by mutableStateOf<WinResult?>(null)
    var gameMode by mutableStateOf(GameMode.TWO_PLAYER)

    val winner: Cell?
        get() = winResult?.winner

    fun play(r: Int, c: Int) {
        if (winner != null) return
        if (board[r][c] != Cell.EMPTY) return

        board = board.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, cell ->
                if (r == rowIndex && c == colIndex) currentPlayer else cell
            }
        }

        winResult = engine.checkWin(board)
        if (winResult == null) {
            currentPlayer = if (currentPlayer == Cell.X) Cell.O else Cell.X

            // If playing against computer and it's computer's turn
            if (gameMode == GameMode.VS_COMPUTER && currentPlayer == Cell.O) {
                computerMove()
            }
        }
    }

    fun computerMove() {
        if (winner != null) return

        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in board.indices) {
            for (c in board[r].indices) {
                if (board[r][c] == Cell.EMPTY) {
                    emptyCells.add(Pair(r, c))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            // Computer tries to lose - it makes random moves without any strategy
            val randomMove = emptyCells.random()
            play(randomMove.first, randomMove.second)
        }
    }

    fun reset() {
        board = engine.emptyBoard()
        winResult = null
        currentPlayer = Cell.X

        // If playing against computer and computer goes first
        if (gameMode == GameMode.VS_COMPUTER && currentPlayer == Cell.O) {
            computerMove()
        }
    }

    // Renamed to avoid clash with the property setter
    fun changeGameMode(mode: GameMode) {
        gameMode = mode
        reset()
    }
}


