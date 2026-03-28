package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.game.Cell
import org.example.project.game.GameViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val vm: GameViewModel = koinInject()
    MaterialTheme {
        Column(Modifier.fillMaxSize().padding(16.dp)) {


            Text("6-in-a-Row TicTacToe", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))


            GameScreen(vm)


            Spacer(Modifier.height(16.dp))
            if (vm.winner != null) {
                Text("Winner: ${vm.winner}")
                Button(onClick = vm::reset) { Text("Restart") }
            }
        }
    }
}

@Composable
fun BoardGrid(vm: GameViewModel) {
    Column {
        vm.board.forEachIndexed { r, row ->
            Row {
                row.forEachIndexed { c, cell ->
                    Box(
                        Modifier
                            .size(40.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                            .clickable { vm.play(r, c) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            when (cell) {
                                Cell.X -> "X"
                                Cell.O -> "O"
                                else -> ""
                            },
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}