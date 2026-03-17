package woowacourse.kanban.board.ui.taskcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskState

@Composable
fun rememberTaskCardCreationState(
    title: String = "",
    content: String = "",
    tags: String = "",
    selectedState: TaskState = TaskState.TO_DO,
    selectedAuthor: String = "",
) = remember {
    TaskCardCreationState(title, content, tags, selectedState, selectedAuthor)
}

@Stable
class TaskCardCreationState(
    title: String = "",
    content: String = "",
    tags: String = "",
    selectedState: TaskState = TaskState.TO_DO,
    selectedAuthor: String = "",
    isTitleInitialized: Boolean = false,
) {
    var title by mutableStateOf(title)
        private set
    var isTitleInitialized by mutableStateOf(isTitleInitialized)
        private set
    val titleValidationState by derivedStateOf {
        if (!this.isTitleInitialized) TitleValidationState.INIT
        else if (Task.isValidTitle(this.title)) TitleValidationState.VALID
        else TitleValidationState.EMPTY_ERROR
    }
    var content by mutableStateOf(content)
    var tags by mutableStateOf(tags)

    val tagValidationState by derivedStateOf {
        val splitTags = this.tags.split(",").map { tag -> tag.trim() }
        when {
            this.tags.isEmpty() -> TagValidationState.VALID
            splitTags.any { tag -> tag.isEmpty() } -> TagValidationState.FORMAT_ERROR
            !Task.isValidTags(splitTags) -> TagValidationState.SIZE_OR_COUNT_ERROR
            else -> TagValidationState.VALID
        }
    }

    var selectedState by mutableStateOf(selectedState)
    var selectedAuthor by mutableStateOf(selectedAuthor)
    val isNewTaskEnabled: Boolean
        get() = when (titleValidationState) {
            TitleValidationState.INIT -> !tagValidationState.isError
            TitleValidationState.VALID -> !tagValidationState.isError
            TitleValidationState.EMPTY_ERROR -> false
        }

    var isCreateButtonClicked by mutableStateOf(false)
        private set

    fun updateTitle(title: String) {
        this.title = title
        this.isTitleInitialized = true
    }

    fun updateCreateButtonClicked(isCreateButtonClicked: Boolean) {
        this.isCreateButtonClicked = isCreateButtonClicked
        this.isTitleInitialized = true
    }
}
