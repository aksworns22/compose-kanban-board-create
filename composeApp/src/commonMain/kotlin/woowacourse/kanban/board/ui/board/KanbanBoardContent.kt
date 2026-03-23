package woowacourse.kanban.board.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlin.math.round
import kotlinx.coroutines.launch
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Progress
import woowacourse.kanban.board.domain.TaskGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.TaskTransitionSnapshot
import woowacourse.kanban.board.ui.creation.CreateTaskCardScreen
import woowacourse.kanban.board.ui.creation.TaskCardCreationState
import woowacourse.kanban.board.ui.taskcard.TaskCard

@Composable
fun KanbanBoardScreen(kanbanBoardState: KanbanBoardState, authors: AuthorGroup) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        if (kanbanBoardState.isDialogOpened) {
            Dialog(
                onDismissRequest = { kanbanBoardState.isDialogOpened = false },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
            ) {
                CreateTaskCardScreen(
                    authors = authors,
                    taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                    onClose = { kanbanBoardState.isDialogOpened = false },
                    onCreate = { task ->
                        kanbanBoardState.taskTransitionSnapshot =
                            kanbanBoardState.taskTransitionSnapshot.transition(task, task.taskState)
                        kanbanBoardState.isDialogOpened = false
                        scope.launch {
                            snackbarHostState.showSnackbar("새로운 태스크가 추가되었습니다.", withDismissAction = true)
                        }
                    },
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                )
            }
        }

        KanbanBoardContent(
            kanbanBoardState = kanbanBoardState,
            modifier = Modifier.padding(horizontal = 16.dp).padding(paddingValues),
        )
    }
}

@Composable
fun KanbanBoardContent(kanbanBoardState: KanbanBoardState, modifier: Modifier = Modifier) {
    val progress = kanbanBoardState.taskTransitionSnapshot.progress
    Column(modifier = modifier) {
        KanbanBoardHeader(
            isDialogOpened = { kanbanBoardState.isDialogOpened = true },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
        )
        Text(
            text = "완료율: ${progress.toPercentage()}% (${progress.completed}/${progress.total})",
            fontSize = 14.sp,
            color = Color(0xFF6A7282),
            modifier = Modifier.semantics { contentDescription = "작업 진행률" },
        )
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { progress.toRatio() },
            color = Color(0xFF4F39F6),
            modifier = Modifier.semantics { contentDescription = "작업 진행률 프로그래스바" }.fillMaxWidth().height(8.dp)
                .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(16.dp)),
            drawStopIndicator = { },
        )
        Spacer(modifier = Modifier.height(20.dp))
        SameStateTaskCardGroups(taskTransitionSnapshot = kanbanBoardState.taskTransitionSnapshot)
    }
}

@Composable
private fun SameStateTaskCardGroups(taskTransitionSnapshot: TaskTransitionSnapshot, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
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
                        .padding(vertical = 16.dp, horizontal = 16.dp),
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
            modifier = Modifier.semantics { contentDescription = "${taskState.toDisplayName()} 태스크 가드 개수" }
                .background(Color.White, shape = RoundedCornerShape(999.dp)).padding(horizontal = 10.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun TaskCardGroupContent(taskGroup: TaskGroup, modifier: Modifier = Modifier) {
    val taskState = taskGroup.type
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.semantics { contentDescription = "${taskState.toDisplayName()} 목록" },
    ) {
        items(count = taskGroup.size) { index ->
            val task = taskGroup[index]
            TaskCard(task = task)
        }
    }
}

@Composable
private fun KanbanBoardHeader(isDialogOpened: () -> Unit, modifier: Modifier = Modifier) {
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

private fun Progress.toPercentage(): Int = round((this.toRatio()) * 100).toInt()

private fun Progress.toRatio(): Float = if (this.total != 0) this.completed.toFloat() / this.total.toFloat() else 0f

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
