package woowacourse.kanban.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.ui.KanbanBoardContent
import woowacourse.kanban.board.ui.KanbanBoardState

@Preview(showBackground = true)
@Composable
fun App() {
    val kanbanBoardState = remember { KanbanBoardState(isNewTaskButtonClicked = false) }
    KanbanBoardContent(kanbanBoardState)
}
