package org.example.project.game

class GameEngine {
    private val size = 10
    private val winLength = 6

    fun emptyBoard(): List<List<Cell>> =
        List(size) { List(size) { Cell.EMPTY } }

    fun checkWin(board: List<List<Cell>>): WinResult? {
        for (r in 0 until size) for (c in 0 until size) {
            val cell = board[r][c]
            if (cell == Cell.EMPTY) continue

            checkDir(board, r, c, 1, 0, cell)?.let { return it }
            checkDir(board, r, c, 0, 1, cell)?.let { return it }
            checkDir(board, r, c, 1, 1, cell)?.let { return it }
            checkDir(board, r, c, 1, -1, cell)?.let { return it }
        }
        return null
    }

    private fun checkDir(board: List<List<Cell>>, r: Int, c: Int, dr: Int, dc: Int, cell: Cell): WinResult? {
        var count = 0
        var nr = r
        var nc = c
        val winningPositions = mutableListOf<Pair<Int, Int>>()

        while (nr in 0 until size && nc in 0 until size && board[nr][nc] == cell) {
            count++
            winningPositions.add(Pair(nr, nc))
            if (count == winLength) return WinResult(cell, winningPositions)
            nr += dr
            nc += dc
        }
        return null
    }
}

data class WinResult(val winner: Cell, val winningPositions: List<Pair<Int, Int>>)

enum class Cell {
    X,
    O,
    EMPTY;


    override fun toString(): String {
        return when (this) {
            X -> "X"
            O -> "O"
            EMPTY -> ""
        }
    }
}