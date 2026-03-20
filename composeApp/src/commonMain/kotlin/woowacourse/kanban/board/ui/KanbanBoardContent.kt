package woowacourse.kanban.board.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.ui.taskcard.TaskCard
import java.util.function.IntFunction

@Stable
class KanbanBoardState(isDialogOpened: Boolean, taskTransitionSnapshot: TaskTransitionSnapshot = TaskTransitionSnapshot(
    tasks = TaskState.entries.associateWith {
        TaskGroup(
            it,
            emptyList(),
        )
    },
)) {
    var isDialogOpened by mutableStateOf(isDialogOpened)
    var taskTransitionSnapshot by mutableStateOf(taskTransitionSnapshot)
}

@Immutable
data class TaskTransitionSnapshot(val tasks: Map<TaskState, TaskGroup>) {
    init {
        TaskState.entries.forEach { taskState ->
            require(tasks.containsKey(taskState)) { "$taskState 가 없습니다." }
        }
    }

    operator fun get(key: TaskState): TaskGroup = tasks.getValue(key)

    fun transition(targetTask: Task, destinationTaskState: TaskState): TaskTransitionSnapshot {
        val originTaskGroup = tasks.getValue(targetTask.taskState)
        val destinationTaskGroup = tasks.getValue(destinationTaskState)
        return TaskTransitionSnapshot(
            tasks + (targetTask.taskState to originTaskGroup.remove(targetTask)) + (destinationTaskState to destinationTaskGroup.add(
                targetTask.changeState(destinationTaskState),
            )),
        )
    }
}

data class TaskGroup(val type: TaskState, val tasks: List<Task>): List<Task> by tasks {
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

@Composable
fun KanbanBoardContent(kanbanBoardState: KanbanBoardState, dialogScreen: @Composable () -> Unit, modifier: Modifier = Modifier) {
    if (kanbanBoardState.isDialogOpened) {
        Dialog(
            onDismissRequest = { kanbanBoardState.isDialogOpened = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
        ) {
            dialogScreen()
        }
    }

    Column {
        Button(onClick = { kanbanBoardState.isDialogOpened = true }, modifier = Modifier) {
            Text("새 태스크 생성")
        }
        Row {
            Column {
                Text("To do")
                Text("${kanbanBoardState.taskTransitionSnapshot[TaskState.TO_DO].size}", modifier = Modifier.semantics { contentDescription = "To do 태스크 가드 개수" })
                Column(modifier = Modifier.semantics { contentDescription = "To do 목록"}) {
                    kanbanBoardState.taskTransitionSnapshot[TaskState.TO_DO].forEach { task ->
                        TaskCard(task = task)
                    }
                }
            }
            Column {
                Text("In Progress")
                Text("${kanbanBoardState.taskTransitionSnapshot[TaskState.IN_PROGRESS].size}", modifier = Modifier.semantics { contentDescription = "In progress 태스크 가드 개수" })
                Column(modifier = Modifier.semantics { contentDescription = "In progress 목록"}) {
                    kanbanBoardState.taskTransitionSnapshot[TaskState.IN_PROGRESS].forEach { task ->
                        TaskCard(task = task)
                    }
                }
            }
            Column {
                Text("Done")
                Text("${kanbanBoardState.taskTransitionSnapshot[TaskState.DONE].size}", modifier = Modifier.semantics { contentDescription = "Done 태스크 가드 개수" })
                Column(modifier = Modifier.semantics { contentDescription = "Done 목록"}) {
                    kanbanBoardState.taskTransitionSnapshot[TaskState.DONE].forEach { task ->
                        TaskCard(task = task)
                    }
                }
            }
        }
    }

}
