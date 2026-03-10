package woowacourse.kanban.board.ui.taskcard

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class InputWindowTest {

    @Test
    fun `showInputWindow가 true일 때 화면이 노출된다`() = runComposeUiTest {
        setContent {
            InputWindow(
                modifier = Modifier,
                onAddTaskCard = { _, _, _, _ -> true },
                showInputWindow = true,
                onValueChange = {},
                errorMessage = ""
            )
        }

        onNodeWithText("제목: ").assertExists()
        onNodeWithText("내용: ").assertExists()
        onNodeWithText("태그: ").assertExists()
        onNodeWithText("작성자: ").assertExists()
    }

    @Test
    fun `취소 버튼을 클릭하면 콜백을 호출한다`() = runComposeUiTest {
        var closed = false

        setContent {
            InputWindow(
                modifier = Modifier,
                onAddTaskCard = { _, _, _, _ -> true },
                showInputWindow = true,
                onValueChange = { closed = true },
                errorMessage = ""
            )
        }

        onNodeWithText("취소").performClick()
        assertThat(closed).isTrue()
    }

    @Test
    fun `확인 버튼을 클릭하면 콜백을 호출한다`() = runComposeUiTest {
        var success = false

        setContent {
            InputWindow(
                modifier = Modifier,
                onAddTaskCard = { _, _, _, _ -> true },
                showInputWindow = true,
                onValueChange = { success = true },
                errorMessage = ""
            )
        }

        onNodeWithText("확인").performClick()
        assertThat(success).isTrue()
    }

    @Test
    fun `에러 메시지 내용이 존재하면 화면에 노출된다`() = runComposeUiTest {
        setContent {
            InputWindow(
                modifier = Modifier,
                onAddTaskCard = { _, _, _, _ -> true },
                showInputWindow = true,
                onValueChange = {},
                errorMessage = "제목을 입력해주세요"
            )
        }

        onNodeWithText("제목을 입력해주세요").assertExists()
    }
}