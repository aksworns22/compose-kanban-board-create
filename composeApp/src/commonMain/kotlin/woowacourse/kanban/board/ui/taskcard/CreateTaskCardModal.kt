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
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskState

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}

enum class TagValidationState(val isError: Boolean) {
    VALID(false),
    FORMAT_ERROR(true),
    SIZE_OR_COUNT_ERROR(true),
}

enum class TitleValidationState {
    INIT,
    VALID,
    EMPTY_ERROR,
}

@Composable
fun CreateTaskCardModal(authors: AuthorGroup, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var isTitleInitialized by remember { mutableStateOf(false) }
    val titleValidationState by remember {
        derivedStateOf {
            if (!isTitleInitialized) TitleValidationState.INIT
            else if (Task.isValidTitle(title)) TitleValidationState.VALID
            else TitleValidationState.EMPTY_ERROR
        }
    }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    val tagValidationState by remember {
        derivedStateOf {
            val splitTags = tags.split(",").map { tag -> tag.trim() }
            when {
                tags.isEmpty() -> TagValidationState.VALID
                splitTags.any { tag -> tag.isEmpty() } -> TagValidationState.FORMAT_ERROR
                !Task.isValidTags(splitTags) -> TagValidationState.SIZE_OR_COUNT_ERROR
                else -> TagValidationState.VALID
            }
        }
    }
    var selectedState by remember { mutableStateOf(TaskState.TO_DO) }
    var selectedAuthor by remember { mutableStateOf(authors.first()) }
    val isNewTaskEnabled = when (titleValidationState) {
        TitleValidationState.INIT -> true
        TitleValidationState.VALID -> !tagValidationState.isError
        TitleValidationState.EMPTY_ERROR -> false
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CreateTaskHeader(modifier = Modifier.fillMaxWidth())
        HorizontalDivider()
        TitleInputField(
            titleProvider = { title }, isTitleError = titleValidationState,
            onValueChange = {
                title = it
                isTitleInitialized = true
            },
        )
        ContentInputField(content = content, onValueChange = { content = it })
        TagsInputField(
            tagsProvider = { tags }, tagValidationState = tagValidationState,
            onValueChange = {
                tags = it
            },
        )
        TaskStateInputField(
            selectedState = selectedState,
            onStateChanged = { newTaskState ->
                selectedState = newTaskState
            },
        )
        AuthorInputField(
            authors = authors, selectedAuthor = selectedAuthor,
            onAuthorSelected = { newAuthor ->
                selectedAuthor = newAuthor
            },
        )

        HorizontalDivider()

        CreateTaskActionButtons(
            isNewTaskEnabled = isNewTaskEnabled,
            onCreateClick = {
                isTitleInitialized = true
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CreateTaskHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
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
private fun TitleInputField(
    titleProvider: () -> String,
    isTitleError: TitleValidationState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LabeledField(
        modifier = modifier,
        label = "제목 *",
        content = {
            CustomTextField(
                value = titleProvider(),
                onValueChange = onValueChange,
                placeholder = "태스크 제목을 입력하세요",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        when (isTitleError) {
                            TitleValidationState.INIT, TitleValidationState.VALID -> Color(0xFF79747E)
                            TitleValidationState.EMPTY_ERROR -> Color(0xFFB3261E)
                        },
                        RoundedCornerShape(8.dp),
                    ),
                trailingIcon = {
                    when (isTitleError) {
                        TitleValidationState.INIT, TitleValidationState.VALID -> {}
                        TitleValidationState.EMPTY_ERROR -> Icon(
                            imageVector = Icons.Default.Error,
                            tint = Color(0xFFB3261E),
                            contentDescription = "경고",
                        )
                    }
                },
            )
        },
        infoContent = {
            when (isTitleError) {
                TitleValidationState.INIT, TitleValidationState.VALID -> { }
                TitleValidationState.EMPTY_ERROR -> Text(
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
private fun ContentInputField(content: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    LabeledField(
        modifier = modifier,
        label = "설명",
        content = {
            CustomTextField(
                value = content,
                onValueChange = onValueChange,
                placeholder = "태스크에 대한 자세한 설명을 입력하세요",
                singleLine = false,
                modifier = Modifier.fillMaxWidth().heightIn(min = 144.dp),
            )
        },
    )
}

@Composable
private fun TagsInputField(
    tagsProvider: () -> String,
    tagValidationState: TagValidationState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LabeledField(
        modifier = modifier,
        label = "태그",
        content = {
            CustomTextField(
                value = tagsProvider(),
                onValueChange = onValueChange,
                placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (tagValidationState.isError) Color(0xFFB3261E) else Color(0xFF79747E),
                        RoundedCornerShape(8.dp),
                    ),
                trailingIcon = {
                    if (tagValidationState.isError) Icon(
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
                text = when (tagValidationState) {
                    TagValidationState.VALID -> "5자 이내의 태그를 최대 5개까지 등록할 수 있습니다."
                    TagValidationState.FORMAT_ERROR -> "태그 형식이 올바르지 않습니다."
                    TagValidationState.SIZE_OR_COUNT_ERROR -> "태그는 5자 이내로 5개까지만 등록할 수 있습니다."
                },
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                color = if (tagValidationState.isError) Color(0xFFB3261E) else Color(0xFF45454F),
            )
        },
    )
}

@Composable
private fun TaskStateInputField(selectedState: TaskState, onStateChanged: (TaskState) -> Unit, modifier: Modifier = Modifier) {
    LabeledField(
        modifier = modifier,
        label = "상태 *",
        content = {
            TaskStateSelectField(
                selectedState = selectedState,
                onStateChanged = onStateChanged,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}

@Composable
private fun AuthorInputField(
    authors: AuthorGroup,
    selectedAuthor: String,
    onAuthorSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LabeledField(
        modifier = modifier,
        label = "담당자 *",
        content = {
            AuthorSelectField(
                selectedAuthor = selectedAuthor,
                onAuthorSelected = onAuthorSelected,
                authors = authors,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    )
}

@Composable
private fun CreateTaskActionButtons(isNewTaskEnabled: Boolean, onCreateClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
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
            onClick = onCreateClick,
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

private fun TaskState.toDisplayName(): String = when (this) {
    TaskState.TO_DO -> "To Do"
    TaskState.IN_PROGRESS -> "In Progress"
    TaskState.DONE -> "Done"
}

@Composable
private fun TaskStateSelectField(selectedState: TaskState, onStateChanged: (TaskState) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(TaskState.entries.size) {
            Box(
                modifier = Modifier
                    .border(
                        2.dp,
                        if (selectedState == TaskState.entries[it]) Color(0xFF1447E6) else Color(0xFFE5E7EB),
                        RoundedCornerShape(8.dp),
                    )
                    .background(if (selectedState == TaskState.entries[it]) Color(0xFFEFF6FF) else Color.White)
                    .semantics { selected = selectedState == TaskState.entries[it] }
                    .noRippleClickable { onStateChanged(TaskState.entries[it]) },
                content = {
                    Text(
                        text = TaskState.entries[it].toDisplayName(),
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
private fun AuthorSelectField(
    selectedAuthor: String,
    onAuthorSelected: (String) -> Unit,
    authors: AuthorGroup,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(authors.size) { author ->
            Box(
                modifier = Modifier.width(200.dp)
                    .border(2.dp, if (selectedAuthor == authors[author]) Color(0xFF615FFF) else Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                    .background(if (selectedAuthor == authors[author]) Color(0xFFEEF2FF) else Color.White)
                    .semantics { selected = selectedAuthor == authors[author] }
                    .noRippleClickable { onAuthorSelected(authors[author]) },
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
private fun LabeledField(
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
        authors = AuthorGroup(authors = listOf("다이노", "페임스")),
        modifier = Modifier
            .background(Color.White).padding(16.dp),
    )
}
