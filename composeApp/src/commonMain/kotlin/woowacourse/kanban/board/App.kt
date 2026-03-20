package woowacourse.kanban.board

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.ui.board.KanbanBoardContent
import woowacourse.kanban.board.ui.board.KanbanBoardState
import woowacourse.kanban.board.ui.creation.CreateTaskCardScreen
import woowacourse.kanban.board.ui.creation.TaskCardCreationState

@Preview(showBackground = true)
@Composable
fun App() {
    val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = false) }
    val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
    KanbanBoardContent(
        kanbanBoardState = kanbanBoardState,
        dialogScreen = {
            CreateTaskCardScreen(
                authors = authors,
                taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                onClose = { kanbanBoardState.isDialogOpened = false },
                onCreate = { task ->
                    kanbanBoardState.taskTransitionSnapshot = kanbanBoardState.taskTransitionSnapshot.transition(task, task.taskState)
                    kanbanBoardState.isDialogOpened = false
                },
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            )
        },
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}
