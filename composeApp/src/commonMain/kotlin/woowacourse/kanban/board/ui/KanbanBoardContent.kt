package woowacourse.kanban.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.ui.creation.CreateTaskCardContent
import woowacourse.kanban.board.ui.creation.TaskCardCreationState

@Stable
class KanbanBoardState(
    isDialogOpened: Boolean,
) {
    var isDialogOpened by mutableStateOf(isDialogOpened)
    var taskGroup by mutableStateOf(mutableListOf<Task>())
}

@Composable
fun CreateTaskCardScreen(authors: AuthorGroup, taskCardCreationState: TaskCardCreationState, onClose: () -> Unit, onCreate: (Task) -> Unit, modifier: Modifier = Modifier) {
    val taskCardCreationState = remember { taskCardCreationState }
    CreateTaskCardContent(
        taskCardCreationState = taskCardCreationState,
        authors = authors,
        onClose = onClose,
        onCreate = onCreate,
        modifier = modifier.semantics { contentDescription = "새 태스크 생성" }.width(672.dp).background(Color.White).padding(16.dp),
    )
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
