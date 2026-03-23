package woowacourse.kanban.board.ui.board

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskGroup

@Stable
class KanbanBoardState(
    isNewTaskDialogOpened: Boolean,
    taskGroup: TaskGroup = TaskGroup(tasks = emptyList())
) {
    var isNewTaskDialogOpened by mutableStateOf(isNewTaskDialogOpened)
        private set
    var taskGroup by mutableStateOf(taskGroup)
        private set

    fun openNewTaskDialog() {
        this@KanbanBoardState.isNewTaskDialogOpened = true
    }

    fun closeNewTaskDialog() {
        this@KanbanBoardState.isNewTaskDialogOpened = false
    }

    fun addNewTask(task: Task) {
        taskGroup = taskGroup.add(task)
    }
}
