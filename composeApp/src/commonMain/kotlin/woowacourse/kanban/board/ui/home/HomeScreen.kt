package woowacourse.kanban.board.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import woowacourse.kanban.board.ui.home.components.AddButton
import woowacourse.kanban.board.ui.home.components.TaskCards
import woowacourse.kanban.board.ui.taskcard.InputWindow

@Composable
fun HomeScreen(
    viewModel : HomeViewModel = viewModel(factory = homeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showInputWindow by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TaskCards(uiState.tasks)
        AddButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            { showInputWindow = true }
        )
        InputWindow(
            modifier = Modifier.align(Alignment.Center),
            { title, content, tags, author -> viewModel.addTask(title, content, tags, author) },
            showInputWindow,
            {
                viewModel.clearErrorMessage()
                showInputWindow = false
            },
            uiState.errorMessage
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() = HomeScreen()