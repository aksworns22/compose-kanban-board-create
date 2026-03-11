package woowacourse.kanban.board.ui.taskcard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
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
import woowacourse.kanban.board.domain.TaskState

@Composable
fun CreateTaskCardModal(authors: List<String>, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var isTagsError by remember { mutableStateOf(false) }
    var isTagFormatError by remember { mutableStateOf(false) }
    var taskState by remember { mutableStateOf(TaskState.TO_DO) }
    var selectedAuthor by remember { mutableStateOf(authors.first()) }

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
        TagsField(
            tags = tags,
            onValueChange = {
                tags = it
                val splitTags = tags.split(",").map { tag -> tag.trim() }
                if(tags.isEmpty()) {
                    isTagFormatError = false
                    isTagsError = false
                } else {
                    isTagFormatError = splitTags.any { tag -> tag.isEmpty() }
                    isTagsError = !Task.isValidTags(splitTags)
                }
            },
            isError = isTagsError,
            isTagFormatError = isTagFormatError,
            modifier = Modifier.background(Color.White),
        )
        TaskStateField(
            selectedState = taskState,
            onStateChanged = { newTaskState -> taskState = newTaskState }
        )
        AuthorField(
            selectedAuthor = selectedAuthor,
            onAuthorSelected = { newAuthor -> selectedAuthor = newAuthor },
            authors = authors
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

@Composable
private fun TagsField(
    tags: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isTagFormatError: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text("태그")
        TextField(
            modifier = modifier,
            value = tags,
            onValueChange = onValueChange,
            isError = isError,
            placeholder = { Text("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)") },
            supportingText = {
                if (isTagFormatError) Text("태그 형식이 올바르지 않습니다.")
                else if (isError) Text("태그는 5자 이내로 5개까지만 등록할 수 있습니다.")
            }
        )
    }
}
@Composable
private fun TaskStateField(
    selectedState: TaskState,
    onStateChanged: (TaskState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Button(onClick = { onStateChanged(TaskState.TO_DO) }) {
            Text("To Do", color = if (selectedState == TaskState.TO_DO) Color.Red else Color.Black)
        }

        Button(onClick = { onStateChanged(TaskState.IN_PROGRESS) }) {
            Text("In Progress", color = if (selectedState == TaskState.IN_PROGRESS) Color.Red else Color.Black)
        }

        Button(onClick = { onStateChanged(TaskState.DONE) }) {
            Text("Done", color = if (selectedState == TaskState.DONE) Color.Red else Color.Black)
        }
    }
}

@Composable
private fun AuthorField(
    selectedAuthor: String,
    onAuthorSelected: (String) -> Unit,
    authors: List<String>
) {
    LazyRow {
        items(authors.size) { author ->
            Text(
                text = authors[author],
                color = if (selectedAuthor == authors[author]) Color.Red else Color.Black,
                modifier = Modifier.clickable { onAuthorSelected(authors[author]) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCreateTaskCardModal() {
    CreateTaskCardModal(authors = listOf("다이노", "페임스"), modifier = Modifier.background(Color(0xFFE5E7EB)))
}