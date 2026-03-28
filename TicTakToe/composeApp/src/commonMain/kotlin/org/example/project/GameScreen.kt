package org.example.project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.game.Cell
import org.example.project.game.GameMode
import org.example.project.game.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game mode selection
        GameModeSelector(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Game status
        GameStatus(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Game board
        Board(
            board = viewModel.board,
            winningPositions = viewModel.winResult?.winningPositions ?: emptyList(),
            onCellClick = { r, c -> viewModel.play(r, c) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reset button
        Button(onClick = { viewModel.reset() }) {
            Text("Reset Game")
        }
    }
}

@Composable
fun GameModeSelector(viewModel: GameViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { viewModel.changeGameMode(GameMode.TWO_PLAYER) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.gameMode == GameMode.TWO_PLAYER) Color.Blue else Color.Gray
            )
        ) {
            Text("Two Players")
        }
        Button(
            onClick = { viewModel.changeGameMode(GameMode.VS_COMPUTER) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.gameMode == GameMode.VS_COMPUTER) Color.Blue else Color.Gray
            )
        ) {
            Text("VS Computer")
        }
    }
}

@Composable
fun GameStatus(viewModel: GameViewModel) {
    when {
        viewModel.winner != null -> {
            Text(
                text = "Player ${viewModel.winner!!} Wins!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Green
            )
        }
        viewModel.board.flatten().none { it == Cell.EMPTY } -> {
            Text(
                text = "Game Draw!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Yellow
            )
        }
        else -> {
            Text(
                text = "Current Player: ${viewModel.currentPlayer}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun Board(
    board: List<List<Cell>>,
    winningPositions: List<Pair<Int, Int>>,
    onCellClick: (Int, Int) -> Unit
) {
    val cellSize = 30.dp
    val boardSize = board.size

    Box {
        // Draw the game board with Canvas
        BoardCanvas(
            board = board,
            winningPositions = winningPositions,
            cellSize = cellSize,
            boardSize = boardSize
        )

        // Invisible clickable overlay
        BoardClickOverlay(
            board = board,
            cellSize = cellSize,
            onCellClick = onCellClick
        )
    }
}

@Composable
fun BoardCanvas(
    board: List<List<Cell>>,
    winningPositions: List<Pair<Int, Int>>,
    cellSize: Dp,
    boardSize: Int
) {
    Canvas(
        modifier = Modifier
            .size(cellSize * boardSize)
            .background(Color.LightGray)
    ) {
        // Draw grid lines
        drawGridLines(boardSize)

        // Draw X and O
        drawCells(board, boardSize)

        // Draw winning line
        drawWinningLine(winningPositions, boardSize)
    }
}

@Composable
fun BoardClickOverlay(
    board: List<List<Cell>>,
    cellSize: Dp,
    onCellClick: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(cellSize * board.size)
    ) {
        for (r in board.indices) {
            for (c in board[r].indices) {
                Box(
                    modifier = Modifier
                        .size(cellSize)
                        .offset(x = cellSize * c, y = cellSize * r)
                        .clickable { onCellClick(r, c) }
                )
            }
        }
    }
}

private fun DrawScope.drawGridLines(boardSize: Int) {
    for (i in 0..boardSize) {
        // Vertical lines
        drawLine(
            color = Color.Black,
            start = Offset(
                x = i * size.width / boardSize,
                y = 0f
            ),
            end = Offset(
                x = i * size.width / boardSize,
                y = size.height
            ),
            strokeWidth = 2f
        )
        // Horizontal lines
        drawLine(
            color = Color.Black,
            start = Offset(
                x = 0f,
                y = i * size.height / boardSize
            ),
            end = Offset(
                x = size.width,
                y = i * size.height / boardSize
            ),
            strokeWidth = 2f
        )
    }
}

private fun DrawScope.drawCells(
    board: List<List<Cell>>,
    boardSize: Int
) {
    for (r in board.indices) {
        for (c in board[r].indices) {
            val cell = board[r][c]
            if (cell != Cell.EMPTY) {
                val centerX = (c + 0.5f) * size.width / boardSize
                val centerY = (r + 0.5f) * size.height / boardSize
                val radius = size.width / boardSize / 3

                if (cell == Cell.X) {
                    // Draw X
                    drawLine(
                        color = Color.Red,
                        start = Offset(
                            x = centerX - radius,
                            y = centerY - radius
                        ),
                        end = Offset(
                            x = centerX + radius,
                            y = centerY + radius
                        ),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.Red,
                        start = Offset(
                            x = centerX + radius,
                            y = centerY - radius
                        ),
                        end = Offset(
                            x = centerX - radius,
                            y = centerY + radius
                        ),
                        strokeWidth = 3f
                    )
                } else {
                    // Draw O
                    drawCircle(
                        color = Color.Blue,
                        center = Offset(centerX, centerY),
                        radius = radius,
                        style = Stroke(width = 3f)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawWinningLine(
    winningPositions: List<Pair<Int, Int>>,
    boardSize: Int
) {
    if (winningPositions.size >= 2) {
        val first = winningPositions.first()
        val last = winningPositions.last()

        val startX = (first.second + 0.5f) * size.width / boardSize
        val startY = (first.first + 0.5f) * size.height / boardSize
        val endX = (last.second + 0.5f) * size.width / boardSize
        val endY = (last.first + 0.5f) * size.height / boardSize

        drawLine(
            color = Color.Green,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 8f
        )
    }
}
