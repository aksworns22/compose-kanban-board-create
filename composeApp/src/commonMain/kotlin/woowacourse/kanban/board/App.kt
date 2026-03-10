package woowacourse.kanban.board

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.data.tasksData
import woowacourse.kanban.board.ui.taskcard.TaskCards

@Preview(showBackground = true)
@Composable
fun App() {
    TaskCards(tasksData)
}
