package woowacourse.kanban.board.domain

import androidx.compose.runtime.Immutable

@Immutable
data class TaskGroup(val tasks: Set<Task>) {
    val size: Int = tasks.size
    fun getSameStateTasks(taskState: TaskState): List<Task> = tasks.filter { it.taskState == taskState }
    fun add(task: Task) = TaskGroup(tasks + task)
}
