package woowacourse.kanban.board.ui.board

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskGroup

@Stable
class KanbanBoardState(isNewTaskDialogOpened: Boolean, taskGroup: TaskGroup = TaskGroup(tasks = emptySet())) {
    var isNewTaskDialogOpened by mutableStateOf(isNewTaskDialogOpened)
        private set
    var taskGroup by mutableStateOf(taskGroup)
        private set

    var beforeTaskGroup by mutableStateOf(taskGroup)
        private set

    fun openNewTaskDialog() {
        this.isNewTaskDialogOpened = true
    }

    fun closeNewTaskDialog() {
        this.isNewTaskDialogOpened = false
    }

    fun addNewTask(task: Task) {
        beforeTaskGroup = taskGroup
        taskGroup = taskGroup.add(task)
    }
}
