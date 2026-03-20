package woowacourse.kanban.board.domain

import androidx.compose.runtime.Immutable
import java.util.function.IntFunction

@Immutable
data class TaskGroup(val type: TaskState, val tasks: List<Task>) : List<Task> by tasks {
    init {
        require(tasks.all { task -> task.taskState == type })
    }

    fun remove(task: Task): TaskGroup {
        if (task !in tasks) return this
        return TaskGroup(type, tasks - task)
    }

    fun add(task: Task): TaskGroup = TaskGroup(type, tasks + task)

    override fun <T : Any?> toArray(p0: IntFunction<Array<out T?>?>): Array<out T?>? {
        throw IllegalStateException("누구세요?(toArray)")
    }
}
