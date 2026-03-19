package woowacourse.kanban.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.ui.CreateTaskCardScreen
import woowacourse.kanban.board.ui.KanbanBoardContent
import woowacourse.kanban.board.ui.KanbanBoardState
import woowacourse.kanban.board.ui.creation.TaskCardCreationState

@Preview(showBackground = true)
@Composable
fun App() {
    val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = false) }
    KanbanBoardContent(kanbanBoardState = kanbanBoardState, dialogScreen = {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = true) }
        KanbanBoardContent(
            kanbanBoardState = kanbanBoardState,
            dialogScreen = {
                CreateTaskCardScreen(
                    authors = authors,
                    taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                    onClose = { kanbanBoardState.isDialogOpened = false },
                )
            },
        )
    })
}
