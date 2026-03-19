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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.ui.creation.CreateTaskCardContent
import woowacourse.kanban.board.ui.creation.TaskCardCreationState

@Stable
class KanbanBoardState(
    isCreateTaskDialogDisplayed: Boolean,
) {
    var isCreateTaskDialogDisplayed by mutableStateOf(isCreateTaskDialogDisplayed)
}

@Composable
fun KanbanBoardContent(kanbanBoardState: KanbanBoardState, modifier: Modifier = Modifier) {
    val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
    if (kanbanBoardState.isCreateTaskDialogDisplayed) {
        Dialog(
            onDismissRequest = { kanbanBoardState.isCreateTaskDialogDisplayed = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
        ) {
            val taskCardCreationState = remember { TaskCardCreationState(selectedAuthor = authors.first()) }
            CreateTaskCardContent(
                taskCardCreationState = taskCardCreationState,
                authors = authors,
                onClose = { kanbanBoardState.isCreateTaskDialogDisplayed = false },
                modifier = modifier.semantics { contentDescription = "새 태스크 생성" }.width(672.dp).background(Color.White).padding(16.dp),
            )
        }
    }

    Button(onClick = { kanbanBoardState.isCreateTaskDialogDisplayed = true }, modifier = Modifier) {
        Text("새 태스크 생성")
    }
}

@Composable
@Preview
fun KanbanBoardContentPreview() {
    KanbanBoardContent(kanbanBoardState = KanbanBoardState(isCreateTaskDialogDisplayed = true), modifier = Modifier.width(672.dp))
}
