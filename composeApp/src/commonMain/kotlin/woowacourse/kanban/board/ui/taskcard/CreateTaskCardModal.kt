package woowacourse.kanban.board.ui.taskcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.dp
import woowacourse.kanban.board.domain.Task

@Composable
fun CreateTaskCardModal(modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleField(
            title = title,
            onValueChange = {
                title = it
                isTitleError = !Task.isValidTitle(title)
            },
            isError = isTitleError,
            modifier = Modifier.background(Color.White),
        )
        ContentField(
            content = content,
            onValueChange = { content = it },
            modifier = Modifier
                .background(Color.White)
                .heightIn(min = 144.dp),
        )
    }

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

@Composable
private fun ContentField(
    content: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text("설명")
        TextField(
            modifier = modifier,
            value = content,
            onValueChange = onValueChange,
            placeholder = { Text("태스크에 대한 자세한 설명을 입력하세요") }
        )
    }
}

@Preview
@Composable
private fun PreviewCreateTaskCardModal() {
    CreateTaskCardModal(modifier = Modifier.background(Color(0xFFE5E7EB)))
}