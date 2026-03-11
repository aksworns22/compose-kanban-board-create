package woowacourse.kanban.board

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.ui.taskcard.CreateTaskCardModal

@Preview(showBackground = true)
@Composable
fun App() {
    CreateTaskCardModal(authors = listOf("다이노", "페임스"))
}
