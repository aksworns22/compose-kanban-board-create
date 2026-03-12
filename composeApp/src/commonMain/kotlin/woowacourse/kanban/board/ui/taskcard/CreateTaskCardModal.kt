package woowacourse.kanban.board.ui.taskcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var selectedState by remember { mutableStateOf(TaskState.TO_DO) }
    var selectedAuthor by remember { mutableStateOf(authors.first()) }
    val isNewTaskEnabled by remember { derivedStateOf { !isTitleError && !isTagsError && !isTagFormatError } }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CreateTaskHeader()
        HorizontalDivider()
        TitleInputField(title, isTitleError) {
            title = it
            isTitleError = !Task.isValidTitle(it)
        }
        ContentInputField(content) { content = it }
        TagsInputField(tags, isTagsError, isTagFormatError) {
            tags = it
            val splitTags = tags.split(",").map { tag -> tag.trim() }
            if (tags.isEmpty()) {
                isTagFormatError = false
                isTagsError = false
            } else {
                isTagFormatError = splitTags.any { tag -> tag.isEmpty() }
                isTagsError = !Task.isValidTags(splitTags)
            }
        }
        TaskStateInputField(selectedState) { newTaskState ->
            selectedState = newTaskState
        }
        AuthorInputField(authors, selectedAuthor) { newAuthor ->
            selectedAuthor = newAuthor
        }

        HorizontalDivider()

        CreateTaskActionButtons(isNewTaskEnabled) {
            isTitleError = !Task.isValidTitle(title)
        }
    }
}

@Composable
private fun CreateTaskHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "새 태스크 생성",
            fontSize = 20.sp,
            color = Color(0xFF101828),
            fontWeight = FontWeight.W600,
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
        )
    }
}


@Composable
private fun TitleInputField(title: String, isTitleError: Boolean, onValueChange: (String) -> Unit) {
    TextInputField(
        label = "제목 *",
        content = {
            CustomTextField(
                value = title,
                onValueChange = onValueChange,
                placeholder = "태스크 제목을 입력하세요",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isTitleError) Color(0xFFB3261E) else Color(0xFF79747E), RoundedCornerShape(8.dp)),
                trailingIcon = { if (isTitleError) Icon(Icons.Default.Error, tint = Color(0xFFB3261E), contentDescription = "경고") },
            )
        },
        infoContent = {
            if (isTitleError) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                    text = "제목을 입력해주세요.",
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    color = Color(0xFFB3261E),
                )
            }
        },
    )
}

@Composable
private fun ContentInputField(content: String, onValueChange: (String) -> Unit) {
    TextInputField(
        label = "설명",
        content = {
            CustomTextField(
                value = content,
                onValueChange = onValueChange,
                placeholder = "태스크에 대한 자세한 설명을 입력하세요",
                singleLine = false,
                modifier = Modifier.heightIn(min = 144.dp),
            )
        },
    )
}

@Composable
private fun TagsInputField(tags: String, isTagsError: Boolean, isTagFormatError: Boolean, onValueChange: (String) -> Unit) {
    TextInputField(
        label = "태그",
        content = {
            CustomTextField(
                value = tags,
                onValueChange = onValueChange,
                placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isTagsError || isTagFormatError) Color(0xFFB3261E) else Color(0xFF79747E),
                        RoundedCornerShape(8.dp),
                    ),
                trailingIcon = {
                    if (isTagsError || isTagFormatError) Icon(
                        Icons.Default.Error,
                        tint = Color(0xFFB3261E),
                        contentDescription = "경고",
                    )
                },
            )
        },
        infoContent = {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = when {
                    isTagFormatError -> "태그 형식이 올바르지 않습니다."
                    isTagsError -> "태그는 5자 이내로 5개까지만 등록할 수 있습니다."
                    else -> "5자 이내의 태그를 최대 5개까지 등록할 수 있습니다."
                },
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                color = if (isTagFormatError || isTagsError) Color(0xFFB3261E) else Color(0xFF45454F),
            )
        },
    )
}

@Composable
private fun TaskStateInputField(selectedState: TaskState, onStateChanged: (TaskState) -> Unit) {
    TextInputField(
        label = "상태 *",
        content = {
            TaskStateSelectField(
                selectedState = selectedState,
                onStateChanged = onStateChanged,
            )
        },
    )
}

@Composable
private fun AuthorInputField(authors: List<String>, selectedAuthor: String, onAuthorSelected: (String) -> Unit) {
    TextInputField(
        label = "담당자 *",
        content = {
            AuthorSelectField(
                selectedAuthor = selectedAuthor,
                onAuthorSelected = onAuthorSelected,
                authors = authors,
            )
        },
    )
}

@Composable
private fun CreateTaskActionButtons(isNewTaskEnabled: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF364153),
            ),
        ) {
            Text(text = "취소", textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onClick,
            enabled = isNewTaskEnabled,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F39F6),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFA7A4BC),
                disabledContentColor = Color.White,
            ),
        ) {
            Text(text = "생성", textAlign = TextAlign.Center)
        }
    }
}


private fun TaskState.toText(): String = when (this) {
    TaskState.TO_DO -> "To Do"
    TaskState.IN_PROGRESS -> "In Progress"
    TaskState.DONE -> "Done"
}

@Composable
private fun TaskStateSelectField(
    selectedState: TaskState,
    onStateChanged: (TaskState) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(TaskState.entries.size) {
            CustomButton(
                modifier = Modifier
                    .border(
                        2.dp,
                        if (selectedState == TaskState.entries[it]) Color(0xFF1447E6) else Color(0xFFE5E7EB),
                        RoundedCornerShape(8.dp),
                    )
                    .background(if (selectedState == TaskState.entries[it]) Color(0xFFEFF6FF) else Color.White)
                    .semantics { selected = selectedState == TaskState.entries[it] },
                onClick = { onStateChanged(TaskState.entries[it]) },
                content = {
                    Text(
                        text = TaskState.entries[it].toText(),
                        color = if (selectedState == TaskState.entries[it]) Color(0xFF1447E6) else Color.Black,
                        modifier = Modifier.width(200.dp).padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                },
            )
        }
    }
}

@Composable
private fun CustomButton(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        ) { onClick() },
    ) {
        content()
    }
}

@Composable
private fun AuthorSelectField(
    selectedAuthor: String,
    onAuthorSelected: (String) -> Unit,
    authors: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(authors.size) { author ->
            CustomButton(
                modifier = Modifier.width(200.dp)
                    .border(2.dp, if (selectedAuthor == authors[author]) Color(0xFF615FFF) else Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .background(if (selectedAuthor == authors[author]) Color(0xFFEEF2FF) else Color.White)
                    .semantics { selected = selectedAuthor == authors[author] },
                onClick = { onAuthorSelected(authors[author]) },
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            imageVector = Icons.Default.AccountCircle,
                            tint = Color(0xFF838383),
                            contentDescription = "기본 프로필 이미지",
                        )
                        Text(
                            text = authors[author],
                            color = Color(0xFF101828),
                            modifier = Modifier.padding(vertical = 16.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun TextInputField(
    label: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    infoContent: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = Color(0xFF364153),
        )
        Spacer(modifier = Modifier.height(10.dp))
        content()
        infoContent?.invoke()
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF79747E), RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFAAAAAA),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
            )
        },
        singleLine = singleLine,
        trailingIcon = trailingIcon,
    )
}

@Preview(
    widthDp = 672,
)
@Composable
private fun PreviewCreateTaskCardModal() {
    CreateTaskCardModal(
        authors = listOf("다이노", "페임스"),
        modifier = Modifier
            .background(Color.White).padding(16.dp),
    )
}