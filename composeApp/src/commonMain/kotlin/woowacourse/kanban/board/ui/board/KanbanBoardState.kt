package woowacourse.kanban.board.ui.board

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.TaskTransitionSnapshot

@Stable
class KanbanBoardState(
    isDialogOpened: Boolean,
    taskTransitionSnapshot: TaskTransitionSnapshot = TaskTransitionSnapshot(
        tasks = TaskState.entries.associateWith {
            TaskGroup(
                it,
                emptyList(),
            )
        },
    ),
) {
    var isDialogOpened by mutableStateOf(isDialogOpened)
    var taskTransitionSnapshot by mutableStateOf(taskTransitionSnapshot)
}
