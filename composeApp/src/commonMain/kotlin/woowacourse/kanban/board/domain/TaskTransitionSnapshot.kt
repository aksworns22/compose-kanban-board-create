package woowacourse.kanban.board.domain

import androidx.compose.runtime.Immutable

@Immutable
data class TaskTransitionSnapshot(val tasks: Map<TaskState, TaskGroup>) : Map<TaskState, TaskGroup> by tasks {
    init {
        TaskState.entries.forEach { taskState ->
            require(tasks.containsKey(taskState)) { "$taskState 가 없습니다." }
        }
    }

    fun transition(targetTask: Task, destinationTaskState: TaskState): TaskTransitionSnapshot {
        val originTaskGroup = tasks.getValue(targetTask.taskState)
        val destinationTaskGroup = tasks.getValue(destinationTaskState)
        return TaskTransitionSnapshot(
            tasks + (targetTask.taskState to originTaskGroup.remove(targetTask)) + (
                destinationTaskState to destinationTaskGroup.add(
                    targetTask.changeState(destinationTaskState),
                )
                ),
        )
    }
}
