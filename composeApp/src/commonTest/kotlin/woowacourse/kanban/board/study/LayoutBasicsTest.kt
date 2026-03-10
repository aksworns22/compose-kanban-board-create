package woowacourse.kanban.board.study

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlin.test.Test

// 1. 모든 테스트가 성공하도록 만들어보자
// 2. 힌트를 참고하여 Preview를 노출시킨다.
// 3. Preview의 interactive 모드를 활용하여 버튼을 클릭해본다.
@OptIn(ExperimentalTestApi::class)
class LayoutBasicsTest {

    @Test
    @Preview(showBackground = true)
    fun `텍스트 내용이 일치한다`() = runComposeUiTest {
        // given
        val text = "안녕 난 컴포즈야~"
        setContent {
            Text(
                // 바꿔 보세요!
                text = "안녕 난 컴포즈야~",
                color = Color.Blue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }

        // then
        onNodeWithText(text)
            .assertExists()
    }

    @Test
    @Preview(showBackground = true)
    fun `컬럼 내부의 자식 개수와 순서가 동일하다`() = runComposeUiTest {
        // given
        setContent {
            Column(
                modifier = Modifier.testTag("이름")
            ) {
                // 바꿔 보세요!
                Text(text = "깜포즈", color = Color.Yellow)
                Text(text = "끔포즈", color = Color.Green)
                Text(text = "킴포즈", color = Color.Black)
            }
        }

        // then
        onNodeWithTag("이름")
            .onChildren()
            .assertCountEquals(3)
            .onFirst()
            .assert(hasText("깜포즈"))
    }

    @Test
    @Preview(showBackground = true)
    fun `버튼을 클릭하면 비활성화 상태로 변경한다`() = runComposeUiTest {
        // given
        setContent {
            var enabled by remember { mutableStateOf(true) }
            Button(
                onClick = {
                    // 바꿔 보세요!
                    enabled = !enabled
                },
                enabled = enabled,
                modifier = Modifier.testTag("버튼")
            ) {
                Text(text = "클릭해주세요")
            }
        }

        // when
        val button = onNodeWithTag("버튼")
            .performClick()

        // then
        button.assertIsNotEnabled()
    }
}
