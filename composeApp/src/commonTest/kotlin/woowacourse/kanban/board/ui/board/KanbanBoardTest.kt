package woowacourse.kanban.board.ui.board

import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TagGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskGroup
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.Title

@OptIn(ExperimentalTestApi::class)
class KanbanBoardTest {
    @Test
    fun `새 태스크 생성 버튼을 누르면 생성 모달(다이어로그)를 표시한다`() = runComposeUiTest {
        val kanbanBoardState = KanbanBoardState(isNewTaskDialogOpened = false)
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            KanbanBoardScreen(kanbanBoardState, authors)
        }
        onNodeWithContentDescription("새 태스크 추가 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성 다이어로그").assertIsDisplayed()
    }

    @Test
    fun `x 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isNewTaskDialogOpened = true) }
            KanbanBoardScreen(kanbanBoardState, authors)
        }
        onNodeWithContentDescription("x 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성 다이어로그").assertDoesNotExist()
    }

    @Test
    fun `취소 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isNewTaskDialogOpened = true) }
            KanbanBoardScreen(kanbanBoardState, authors)
        }
        onNodeWithContentDescription("취소 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성 다이어로그").assertDoesNotExist()
    }

    @Test
    fun `제목이 존재하고 태그의 형식, 상태가 올바르면 새로운 태스크를 생성한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("디이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(isNewTaskDialogOpened = true)
        setContent {
            KanbanBoardScreen(kanbanBoardState, authors)
        }
        onNodeWithContentDescription("태스크 제목 입력 텍스트 필드").performTextInput("멋진 제목")
        onNodeWithContentDescription("새 태스크 생성 버튼").performClick()

        val expectedTaskGroup = TaskGroup(
            tasks = setOf(
                Task(
                    title = Title("멋진 제목"),
                    content = "",
                    tags = TagGroup(tags = emptyList()),
                    taskState = TaskState.TO_DO,
                    author = authors.first(),
                ),
            ),
        )

        assertThat(kanbanBoardState.taskGroup).isEqualTo(expectedTaskGroup)
    }

    @Test
    fun `생성된 태스크를 각 태스크 상태에 맞게 표시한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val taskGroup = TaskGroup(
            setOf(
                Task(
                    title = Title("해야할 일 제목"),
                    content = "해야할 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                    taskState = TaskState.TO_DO,
                    author = authors.first(),
                ),
                Task(
                    title = Title("진행중인 일 제목"),
                    content = "진행중인 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("진행중인일"))),
                    taskState = TaskState.IN_PROGRESS,
                    author = authors.first(),
                ),
                Task(
                    title = Title("끝난 일 제목"),
                    content = "끝난 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("끝난일"))),
                    taskState = TaskState.DONE,
                    author = authors.first(),
                ),
            ),
        )

        setContent {
            KanbanBoardContent(taskGroup = taskGroup, onNewTaskButtonClick = { })
        }

        onNodeWithContentDescription("To Do 목록")
            .onChildren()[0].assertContentDescriptionEquals("해야할 일 제목에 대한 태스크 카드")
        onNodeWithContentDescription("In Progress 목록")
            .onChildren()[0].assertContentDescriptionEquals("진행중인 일 제목에 대한 태스크 카드")
        onNodeWithContentDescription("Done 목록")
            .onChildren()[0].assertContentDescriptionEquals("끝난 일 제목에 대한 태스크 카드")
    }

    @Test
    fun `각 상태에 따른 태스크 카드의 개수에 따라 올바른 숫자가 표시된다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val taskGroup = TaskGroup(
            tasks = setOf(
                Task(
                    title = Title("해야할 일 제목 1"),
                    content = "해야할 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                    taskState = TaskState.TO_DO,
                    author = authors.first(),
                ),
                Task(
                    title = Title("해야할 일 제목 2"),
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
            ),
        )

        setContent {
            KanbanBoardContent(taskGroup = taskGroup, onNewTaskButtonClick = { })
        }

        onNodeWithContentDescription("To Do 태스크 가드 개수").assertTextEquals("2")
        onNodeWithContentDescription("In Progress 태스크 가드 개수").assertTextEquals("1")
        onNodeWithContentDescription("Done 태스크 가드 개수").assertTextEquals("0")
    }

    @Test
    fun `완료율은 소수점 첫째자리에서 반올림한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val taskGroup = TaskGroup(
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
            ),
        )

        setContent {
            KanbanBoardContent(taskGroup = taskGroup, onNewTaskButtonClick = { })
        }

        onNodeWithContentDescription("작업 진행률").assertTextEquals("완료율: 33% (1/3)")
    }

    @Test
    fun `계산된 완료율에 맞게 프로그래스바를 표시한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val taskGroup = TaskGroup(
            setOf(
                Task(
                    title = Title("해야할 일 제목 1"),
                    content = "해야할 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                    taskState = TaskState.TO_DO,
                    author = authors.first(),
                ),
                Task(
                    title = Title("다한 일"),
                    content = "다한 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("한일"))),
                    taskState = TaskState.DONE,
                    author = authors.first(),
                ),
            ),
        )

        setContent {
            KanbanBoardContent(taskGroup = taskGroup, onNewTaskButtonClick = { })
        }

        onNodeWithContentDescription("작업 진행률 프로그래스바")
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.5f, range = 0.0f..1.0f))
    }

    @Test
    fun `새로운 태스크가 생성되었을 때 칸반보드에 스낵바를 표시한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("디이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(isNewTaskDialogOpened = true)
        setContent {
            KanbanBoardScreen(kanbanBoardState = kanbanBoardState, authors = authors)
        }

        onNodeWithContentDescription("태스크 제목 입력 텍스트 필드").performTextInput("뷁르와 함께 멋진 태스크 만들기")
        onNodeWithContentDescription("새 태스크 생성 버튼").performClick()
        onNodeWithText("새로운 태스크가 추가되었습니다.").assertIsDisplayed()
    }

    @Test
    fun `스낵바의 x 버튼을 클릭하면 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("디이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(isNewTaskDialogOpened = true)
        setContent {
            KanbanBoardScreen(kanbanBoardState = kanbanBoardState, authors = authors)
        }

        onNodeWithContentDescription("태스크 제목 입력 텍스트 필드").performTextInput("뷁르와 함께 멋진 태스크 만들기")
        onNodeWithContentDescription("새 태스크 생성 버튼").performClick()
        onNodeWithText("새로운 태스크가 추가되었습니다.").assertIsDisplayed()
        onNodeWithContentDescription("닫기").performClick()
        onNodeWithText("새로운 태스크가 추가되었습니다.").assertDoesNotExist()
    }
}
