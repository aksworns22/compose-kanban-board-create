package woowacourse.kanban.board.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.Task

@Stable
class KanbanBoardState(isDialogOpened: Boolean) {
    var isDialogOpened by mutableStateOf(isDialogOpened)
    var taskGroup by mutableStateOf(mutableListOf<Task>())
}

@Composable
fun KanbanBoardContent(kanbanBoardState: KanbanBoardState, dialogScreen: @Composable () -> Unit, modifier: Modifier = Modifier) {
    if (kanbanBoardState.isDialogOpened) {
        Dialog(
            onDismissRequest = { kanbanBoardState.isDialogOpened = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
        ) {
            dialogScreen()
        }
    }

    Button(onClick = { kanbanBoardState.isDialogOpened = true }, modifier = Modifier) {
        Text("새 태스크 생성")
    }
}
