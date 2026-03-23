package woowacourse.kanban.board.ui.board

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.TaskGroup

@Stable
class KanbanBoardState(
    isDialogOpened: Boolean,
    taskGroup: TaskGroup = TaskGroup(tasks = emptyList())
) {
    var isDialogOpened by mutableStateOf(isDialogOpened)
    var taskGroup by mutableStateOf(taskGroup)
}
