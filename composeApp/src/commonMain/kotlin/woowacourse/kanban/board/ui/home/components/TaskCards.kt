package woowacourse.kanban.board.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.data.datasource.tasksData
import woowacourse.kanban.board.domain.entity.Task

@Composable
fun TaskCards(tasks: List<Task>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks.size) { item ->
            TaskCard(task = tasks[item])
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskCardsPreview() {
    TaskCards(tasks = tasksData)
}