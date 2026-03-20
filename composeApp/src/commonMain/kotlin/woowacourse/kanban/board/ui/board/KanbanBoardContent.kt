package woowacourse.kanban.board.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import woowacourse.kanban.board.domain.Progress
import woowacourse.kanban.board.domain.TaskGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.TaskTransitionSnapshot
import woowacourse.kanban.board.ui.taskcard.TaskCard
import kotlin.math.round

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

    val progress = kanbanBoardState.taskTransitionSnapshot.progress
    Column {
        KanbanBoardHeader(
            progress = kanbanBoardState.taskTransitionSnapshot.progress,
            isDialogOpened = { kanbanBoardState.isDialogOpened = true },
            modifier = modifier.padding(top = 16.dp).fillMaxWidth(),
        )
        Text(
            text = "완료율: ${round((progress.completed.toDouble() / progress.total.toDouble()) * 100).toInt()}% (${progress.completed}/${progress.total})",
            modifier.semantics { contentDescription = "작업 진행률" },
        )
        SameStateTaskCardGroups(taskTransitionSnapshot = kanbanBoardState.taskTransitionSnapshot, modifier = modifier)
    }
}

@Composable
private fun SameStateTaskCardGroups(taskTransitionSnapshot: TaskTransitionSnapshot, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        TaskState.entries.forEach { taskState ->
            val taskGroup = taskTransitionSnapshot.getValue(taskState)
            Column(modifier = Modifier.clip(RoundedCornerShape(10.dp)).taskCardGroupBackground(taskState)) {
                TaskCardGroupHeader(
                    taskGroup = taskGroup,
                    modifier = Modifier.size(width = 320.dp, height = 40.dp)
                        .taskCardGroupHeaderBackground(
                            taskState = taskState,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        )
                        .padding(horizontal = 16.dp),
                )
                TaskCardGroupContent(
                    taskGroup,
                    modifier = Modifier.size(width = 320.dp, height = 700.dp).taskCardGroupBorder(taskState)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun TaskCardGroupHeader(taskGroup: TaskGroup, modifier: Modifier = Modifier) {
    val taskState = taskGroup.type
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        Text(taskState.toDisplayName(), color = Color.White, fontWeight = FontWeight.W600, fontSize = 16.sp)
        Text(
            "${taskGroup.size}",
            modifier = Modifier.semantics { contentDescription = "${taskState.toDisplayName()} 태스크 가드 개수" },
        )
    }
}

@Composable
private fun TaskCardGroupContent(taskGroup: TaskGroup, modifier: Modifier = Modifier) {
    val taskState = taskGroup.type
    Column(
        modifier = modifier.semantics { contentDescription = "${taskState.toDisplayName()} 목록" },
    ) {
        taskGroup.forEach { task ->
            TaskCard(task = task)
        }
    }
}

@Composable
private fun KanbanBoardHeader(progress: Progress, isDialogOpened: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(text = "Compose Desktop 칸반 보드", fontSize = 24.sp, color = Color(0xFF101828))
        Button(
            onClick = isDialogOpened,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F39F6),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFA7A4BC),
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "새 태스크 생성 아이콘")
            Text("새 태스크 생성")
        }
    }
}

private fun TaskState.toDisplayName(): String = when (this) {
    TaskState.TO_DO -> "To Do"
    TaskState.IN_PROGRESS -> "In Progress"
    TaskState.DONE -> "Done"
}

private fun Modifier.taskCardGroupBorder(taskState: TaskState): Modifier {
    val borderColor = when (taskState) {
        TaskState.TO_DO -> Color(0xFFBEDBFF)
        TaskState.IN_PROGRESS -> Color(0xFFFEE685)
        TaskState.DONE -> Color(0xFFB9F8CF)
    }
    return this.border(
        width = 1.dp,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 10.dp, bottomEnd = 10.dp),
        color = borderColor,
    )
}

private fun Modifier.taskCardGroupHeaderBackground(taskState: TaskState, shape: Shape): Modifier {
    val headerBackgroundColor = when (taskState) {
        TaskState.TO_DO -> Color(0xFF155DFC)
        TaskState.IN_PROGRESS -> Color(0xFFE17100)
        TaskState.DONE -> Color(0xFF00A63E)
    }
    return this.background(headerBackgroundColor, shape = shape)
}

private fun Modifier.taskCardGroupBackground(taskState: TaskState, shape: Shape = RectangleShape): Modifier {
    val backgroundColor = when (taskState) {
        TaskState.TO_DO -> Color(0xFFEFF6FF)
        TaskState.IN_PROGRESS -> Color(0xFFFFFBEB)
        TaskState.DONE -> Color(0xFFF0FDF4)
    }
    return this.background(backgroundColor, shape = shape)
}
