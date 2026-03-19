package woowacourse.kanban.board.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TagGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.Title
import woowacourse.kanban.board.ui.creation.TaskCardCreationState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class KanbanBoardTest {
    @Test
    fun `새 태스크 생성 버튼을 누르면 생성 모달(다이어로그)를 표시한다`() = runComposeUiTest {
        val kanbanBoardState = KanbanBoardState(isDialogOpened = true)
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("새 태스크 생성").isDisplayed()
    }

    @Test
    fun `x 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = true) }
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("x 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }

    @Test
    fun `취소 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = true) }
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("취소 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }

    @Test
    fun `제목이 존재하고 태그의 형식, 상태가 올바르면 새로운 태스크를 생성한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("디이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(isDialogOpened = true)
        setContent {
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(
                            title = "멋진 제목",
                            content = "멋진 내용",
                            tags = "멋진, 태그",
                            selectedAuthor = authors.first(),
                            isTitleInitialized = true
                        ),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { task ->
                            kanbanBoardState.taskGroup.add(task)
                            kanbanBoardState.isDialogOpened = false
                        },
                    )
                },
            )
        }
        onNodeWithContentDescription("새 태스크 생성 버튼").performClick()

        assertThat(
            kanbanBoardState.taskGroup.first(),
        ).isEqualTo(
            Task(
                title = Title("멋진 제목"), content = "멋진 내용", tags = TagGroup(tags = listOf(Tag("멋진"), Tag("태그"))),
                taskState = TaskState.TO_DO,
                author = authors.first(),
            ),
        )
    }
}
