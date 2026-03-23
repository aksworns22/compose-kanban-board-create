package woowacourse.kanban.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.ui.board.KanbanBoardScreen
import woowacourse.kanban.board.ui.board.KanbanBoardState

@Preview(showBackground = true)
@Composable
fun App() {
    val kanbanBoardState = remember { KanbanBoardState(isNewTaskDialogOpened = false) }
    val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
    KanbanBoardScreen(kanbanBoardState, authors)
}
