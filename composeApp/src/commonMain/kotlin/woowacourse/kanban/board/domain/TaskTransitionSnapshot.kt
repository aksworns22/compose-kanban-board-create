package woowacourse.kanban.board.domain

import androidx.compose.runtime.Immutable

@Immutable
data class TaskTransitionSnapshot(val tasks: Map<TaskState, TaskGroup>) : Map<TaskState, TaskGroup> by tasks {
    init {
        TaskState.entries.forEach { taskState ->
            require(tasks.containsKey(taskState)) { "$taskState 가 없습니다." }
        }
    }

    val progress: Progress = Progress(total = tasks.values.sumOf { it.size }, completed = tasks.getValue(TaskState.DONE).size)

    fun transition(targetTask: Task, destinationTaskState: TaskState): TaskTransitionSnapshot {
        val originTaskGroup = tasks.getValue(targetTask.taskState)
        val destinationTaskGroup = tasks.getValue(destinationTaskState)
        val newOriginTaskGroup = (originTaskGroup.remove(targetTask))
        val newDestinationTaskGroup = destinationTaskGroup.add(targetTask.changeState(destinationTaskState))
        return TaskTransitionSnapshot(
            tasks + (targetTask.taskState to newOriginTaskGroup) + (destinationTaskState to newDestinationTaskGroup),
        )
    }
}

data class Progress(val total: Int, val completed: Int)
