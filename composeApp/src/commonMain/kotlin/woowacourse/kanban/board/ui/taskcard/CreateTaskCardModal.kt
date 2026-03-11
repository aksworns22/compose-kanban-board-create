package woowacourse.kanban.board.ui.taskcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.board.domain.Task

@Composable
fun CreateTaskCardModal() {
    var title by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    TitleField(
        title = title,
        onValueChange = {
            title = it
            isTitleError = !Task.isValidTitle(title)
        },
        isError = isTitleError,
        modifier = Modifier.background(Color.White),
    )
}

@Composable
private fun TitleField(
    title: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text("제목 *")
        TextField(
            value = title,
            onValueChange = onValueChange,
            placeholder = { Text("태스크 제목을 입력하세요") },
            isError = isError,
            supportingText = { if (isError) Text("제목을 입력해주세요.") },
        )
    }
}

@Preview
@Composable
private fun PreviewCreateTaskCardModal() {
    CreateTaskCardModal()
}