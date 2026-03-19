package woowacourse.kanban.board.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
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
                    )
                },
            )
        }
        onNodeWithContentDescription("취소 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }
}
