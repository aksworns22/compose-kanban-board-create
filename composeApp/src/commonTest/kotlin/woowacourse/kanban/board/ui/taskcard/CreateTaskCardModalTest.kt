package woowacourse.kanban.board.ui.taskcard

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CreateTaskCardModalTest {

    @Test
    fun `제목을 입력하지 않으면 에러 문구가 노출된다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = listOf("다이노", "페임스"))
        }

        val typedTitle = "지워질 제목입니다"

        onNodeWithText("태스크 제목을 입력하세요").performTextInput(typedTitle)
        onNodeWithText(typedTitle).performTextClearance()
        onNodeWithText("제목을 입력해주세요.").assertExists()
    }

    @Test
    fun `제목을 입력하지 않으면 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = listOf("다이노", "페임스"))
        }

        val typedTitle = "지워질 제목입니다"

        onNodeWithText("태스크 제목을 입력하세요").performTextInput(typedTitle)
        onNodeWithText(typedTitle).performTextClearance()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 쉼표로 시작하면 형식 에러가 노출된다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = listOf("다이노", "페임스"))
        }

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput(",hello")
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
    }

    @Test
    fun `태그가 쉼표로 끝나면 형식 에러가 노출된다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = listOf("다이노", "페임스"))
        }

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("hello,")
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
    }
}