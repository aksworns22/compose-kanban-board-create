package woowacourse.kanban.board.ui.home.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AddButtonTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `버튼을 클릭하면 콜백을 호출한다`() = runComposeUiTest{
        var closed = false

        setContent {
            AddButton(
                modifier = Modifier,
                onValueChange = { closed = true }
            )
        }

        onNodeWithText("추가").performClick()
        assertThat(closed).isTrue()
    }
}