package woowacourse.kanban.board.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class KanbanBoardTest {
    @Test
    fun `새 태스크 생성 버튼을 누르면 생성 모달(다이어로그)를 표시한다`() = runComposeUiTest {
        val kanbanBoardState = KanbanBoardState(isNewTaskButtonClicked = true)
        setContent {
            KanbanBoardContent(kanbanBoardState = kanbanBoardState)
        }
        onNodeWithContentDescription("새 태스크 생성").isDisplayed()
    }
}
