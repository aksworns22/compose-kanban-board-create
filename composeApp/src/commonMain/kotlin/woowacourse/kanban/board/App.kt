package woowacourse.kanban.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.ui.taskcard.CreateTaskCardModal
import woowacourse.kanban.board.ui.taskcard.rememberTaskCardCreationState

@Preview(showBackground = true)
@Composable
fun App() {
    val authors = AuthorGroup(authors = listOf("다이노", "페임스"))
    val taskCardCreationState = rememberTaskCardCreationState(
        title = "LazyColumn 컴포넌트 구현",
        content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
        tags = "컴포넌트, 성능",
        selectedState = TaskState.TO_DO,
        selectedAuthor = authors.first(),
    )
    CreateTaskCardModal(
        taskCardCreationState = taskCardCreationState,
        authors = authors,
        modifier = Modifier.background(Color.White).padding(16.dp),
    )
}
