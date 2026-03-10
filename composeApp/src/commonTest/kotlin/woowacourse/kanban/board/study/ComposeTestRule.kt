package woowacourse.kanban.board.study

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ComposeTestRule {

//    @Test
//    fun `동기화1`() = runComposeUiTest {
//        var counter by mutableStateOf(0)
//        var latestCounter = 0
//        setContent {
//            Column {
//                Button({ counter++ }) {
//                    Text(counter.toString())
//                }
//                latestCounter = counter
//            }
//        }
//        counter = 1
//        assertThat(counter).isEqualTo(latestCounter)
//    }

//    @Test
//    fun `동기화2`() = runComposeUiTest {
//        var counter by mutableStateOf(0)
//        var latestCounter = 0
//        setContent {
//            Column {
//                Button({ counter++ }) {
//                    Text(counter.toString())
//                }
//                latestCounter = counter
//            }
//        }
//        counter = 1
//        waitForIdle()
//        // Compose UI assertion
//        onNodeWithText("0").assertExists()
//        // JUnit/Assertj assertion
//        assertThat(counter).isEqualTo(latestCounter)
//    }

    @Test
    fun `동기화3`() = runComposeUiTest {
        var counter by mutableStateOf(0)
        var latestCounter = 0
        setContent {
            Column {
                Button({ counter++ }) {
                    Text(counter.toString())
                }
                latestCounter = counter
            }
        }
        counter = 1
        waitForIdle()
        onNodeWithText("1").assertExists()
    }

    @Test
    fun `노드 병합1`() = runComposeUiTest {
        setContent {
            Button({ }, modifier = Modifier.testTag("버튼")) {
                Text("확인")
                Text("버튼")
            }
        }
        onNodeWithText("확인").assertExists()
    }

//    @Test
//    fun `노드 병합2`() = runComposeUiTest {
//        setContent {
//            Button({ }, modifier = Modifier.testTag("버튼")) {
//                Text("확인", modifier = Modifier.testTag("text"))
//                Text("버튼")
//            }
//        }
//        onNodeWithTag("text").assertTextEquals("확인")
//    }

    @Test
    fun `노드 병합3`() = runComposeUiTest {
        setContent {
            Button({ }, modifier = Modifier.testTag("버튼")) {
                Text("확인", modifier = Modifier.testTag("text"))
                Text("버튼")
            }
        }
        onNodeWithTag("버튼").printToLog("로그") // Text = '[확인, 버튼]'
        onNodeWithTag("text", useUnmergedTree = true).assertTextEquals("확인")
    }
}