package woowacourse.kanban.board.domain

import androidx.compose.runtime.Immutable
import java.util.function.IntFunction

@Immutable
data class TaskGroup(val tasks: List<Task>) {
    val size: Int = tasks.size
    fun getSameStateTasks(taskState: TaskState): List<Task> = tasks.filter { it.taskState == taskState }
    fun add(task: Task) = TaskGroup(tasks + task)
}
