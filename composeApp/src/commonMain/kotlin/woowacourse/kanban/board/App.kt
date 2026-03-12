package woowacourse.kanban.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.ui.taskcard.CreateTaskCardModal

@Preview(showBackground = true)
@Composable
fun App() {
    CreateTaskCardModal(
        authors = listOf("다이노", "페임스"),
        modifier = Modifier
            .width(672.dp)
            .background(Color.White)
            .padding(16.dp),
    )
}
