package woowacourse.kanban.board.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class KanbanBoardTest {
    @Test
    fun `새 태스크 생성 버튼을 누르면 생성 모달(다이어로그)를 표시한다`() = runComposeUiTest {
        val kanbanBoardState = KanbanBoardState(isCreateTaskDialogDisplayed = true)
        setContent {
            KanbanBoardContent(kanbanBoardState = kanbanBoardState)
        }
        onNodeWithContentDescription("새 태스크 생성").isDisplayed()
    }

    @Test
    fun `x 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isCreateTaskDialogDisplayed = true) }
            KanbanBoardContent(kanbanBoardState = kanbanBoardState)
        }
        onNodeWithContentDescription("x 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }
}
