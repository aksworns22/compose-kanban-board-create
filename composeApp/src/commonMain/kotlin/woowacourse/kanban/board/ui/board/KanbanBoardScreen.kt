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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.round
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Progress
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TagGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.Title
import woowacourse.kanban.board.ui.creation.CreateTaskCardScreen
import woowacourse.kanban.board.ui.creation.TaskCardCreationState
import woowacourse.kanban.board.ui.taskcard.TaskCard

@Composable
fun KanbanBoardScreen(kanbanBoardState: KanbanBoardState, authors: AuthorGroup) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(kanbanBoardState.taskGroup.size) {
        if (kanbanBoardState.taskGroup.size > kanbanBoardState.beforeTaskGroup.size) {
            snackbarHostState.showSnackbar("새로운 태스크가 추가되었습니다.", withDismissAction = true)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        if (kanbanBoardState.isNewTaskDialogOpened) {
            Dialog(
                onDismissRequest = { kanbanBoardState.closeNewTaskDialog() },
                properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
            ) {
                CreateTaskCardScreen(
                    authors = authors,
                    taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                    onClose = { kanbanBoardState.closeNewTaskDialog() },
                    onCreate = { task ->
                        kanbanBoardState.addNewTask(task)
                        kanbanBoardState.closeNewTaskDialog()
                    },
                    modifier = Modifier.semantics { contentDescription = "새 태스크 생성 다이어로그" }.clip(RoundedCornerShape(10.dp)),
                )
            }
        }

        KanbanBoardContent(
            taskGroup = kanbanBoardState.taskGroup,
            onNewTaskButtonClick = { kanbanBoardState.openNewTaskDialog() },
            modifier = Modifier.padding(horizontal = 16.dp).padding(paddingValues),
        )
    }
}

@Composable
fun KanbanBoardContent(taskGroup: TaskGroup, onNewTaskButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    val progress = Progress.of(taskGroup)
    Column(modifier = modifier) {
        KanbanBoardHeader(
            onNewTaskButtonClick = onNewTaskButtonClick,
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
        SameStateTaskCardGroups(taskGroup)
    }
}

@Composable
private fun SameStateTaskCardGroups(taskGroup: TaskGroup, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        TaskState.entries.forEach { taskState ->
            val sameStateTaskGroup = taskGroup.getSameStateTasks(taskState = taskState)
            Column(modifier = Modifier.clip(RoundedCornerShape(10.dp)).taskCardGroupBackground(taskState)) {
                TaskCardGroupHeader(
                    taskState = taskState,
                    tasks = sameStateTaskGroup,
                    modifier = Modifier.size(width = 320.dp, height = 40.dp)
                        .taskCardGroupHeaderBackground(
                            taskState = taskState,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        )
                        .padding(horizontal = 16.dp),
                )
                TaskCardGroupContent(
                    taskState = taskState,
                    sameStateTaskGroup,
                    modifier = Modifier.size(width = 320.dp, height = 700.dp).taskCardGroupBorder(taskState)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun TaskCardGroupHeader(taskState: TaskState, tasks: List<Task>, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        Text(taskState.toDisplayName(), color = Color.White, fontWeight = FontWeight.W600, fontSize = 16.sp)
        Text(
            "${tasks.size}",
            modifier = Modifier.semantics { contentDescription = "${taskState.toDisplayName()} 태스크 가드 개수" }
                .background(Color.White, shape = RoundedCornerShape(999.dp)).padding(horizontal = 10.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun TaskCardGroupContent(taskState: TaskState, tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.semantics { contentDescription = "${taskState.toDisplayName()} 목록" },
    ) {
        items(items = tasks, key = { it.hashCode() }) {
            TaskCard(task = it)
        }
    }
}

@Composable
private fun KanbanBoardHeader(onNewTaskButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(text = "Compose Desktop 칸반 보드", fontSize = 24.sp, color = Color(0xFF101828))
        Button(
            onClick = onNewTaskButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F39F6),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFA7A4BC),
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.semantics { contentDescription = "새 태스크 추가 버튼" },
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

@Preview(
    widthDp = 1280,
    heightDp = 920,
)
@Composable
fun KanbanBoardScreenPreview() {
    val authors = previewAuthors()
    val kanbanBoardState = KanbanBoardState(
        isNewTaskDialogOpened = false,
        taskGroup = previewTaskGroup(),

    )

    KanbanBoardScreen(kanbanBoardState = kanbanBoardState, authors = authors)
}

@Preview(
    widthDp = 1280,
    heightDp = 920,
)
@Composable
private fun KanbanBoardScreenWithDialogPreview() {
    val authors = previewAuthors()
    val kanbanBoardState = KanbanBoardState(
        isNewTaskDialogOpened = true,
        taskGroup = previewTaskGroup(),
    )

    KanbanBoardScreen(kanbanBoardState = kanbanBoardState, authors = authors)
}

@Preview(
    widthDp = 1280,
    heightDp = 920,
    showBackground = true,
)
@Composable
private fun SameStateTaskCardGroupsPreview() {
    SameStateTaskCardGroups(taskGroup = previewTaskGroup())
}

private fun previewAuthors() = AuthorGroup(listOf(Author("다이노"), Author("페임스")))
private fun previewTaskGroup(authors: AuthorGroup = previewAuthors()) = TaskGroup(
    setOf(
        Task(
            title = Title("해야할 일 제목 1"),
            content = "해야할 일 내용",
            tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
            taskState = TaskState.TO_DO,
            author = authors.first(),
        ),
        Task(
            title = Title("진행중인 일 제목 1"),
            content = "진행중인 일 내용",
            tags = TagGroup(tags = listOf(Tag("멋진"), Tag("진행중인일"))),
            taskState = TaskState.IN_PROGRESS,
            author = authors.first(),
        ),
        Task(
            title = Title("다한 일"),
            content = "다한 일 내용",
            tags = TagGroup(tags = listOf(Tag("멋진"), Tag("한일"))),
            taskState = TaskState.DONE,
            author = authors.first(),
        ),
        Task(
            title = Title("다한 것 같은 일"),
            content = "해치웠나?",
            tags = TagGroup(tags = listOf(Tag("멋진"), Tag("한일"))),
            taskState = TaskState.DONE,
            author = authors.first(),
        ),
    ),
)
