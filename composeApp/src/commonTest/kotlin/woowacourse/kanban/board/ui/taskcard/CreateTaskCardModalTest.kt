package woowacourse.kanban.board.ui.taskcard

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.board.domain.AuthorGroup
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CreateTaskCardModalTest {
    @Test
    fun `제목을 입력하지 않으면 에러 문구가 노출된다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }

        val typedTitle = "지워질 제목입니다"

        onNodeWithText("태스크 제목을 입력하세요").performTextInput(typedTitle)
        onNodeWithText(typedTitle).performTextClearance()
        onNodeWithText("제목을 입력해주세요.").assertExists()
    }

    @Test
    fun `제목을 입력하지 않으면 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }

        val typedTitle = "지워질 제목입니다"

        onNodeWithText("태스크 제목을 입력하세요").performTextInput(typedTitle)
        onNodeWithText(typedTitle).performTextClearance()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 쉼표로 시작하면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").assertIsEnabled()

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput(",hello")
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 쉼표로 끝나면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").assertIsEnabled()

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("hello,")
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `쉼표가 연달아 나오면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").assertIsEnabled()

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("hello,,hi")
        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 5자 이내가 아니라면 태그 규칙 위반 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").assertIsEnabled()

        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("123456")
        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 5개를 초과하면 태그 규칙 위반 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").assertIsEnabled()

        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertDoesNotExist()
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("1, 2, 3, 4, 5, 6")
        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태스크 상태로 첫 번째 요소가 기본으로 선택된다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }

        onNodeWithText("To Do").assertIsSelected()
    }

    @Test
    fun `태스크 상태는 한 항목만 선택 가능하다`() = runComposeUiTest {

        // when

        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("To Do").performClick()
        onNodeWithText("In Progress").performClick()
        onNodeWithText("Done").performClick()

        // then

        onNodeWithText("To Do").assertIsNotSelected()
        onNodeWithText("In Progress").assertIsNotSelected()
        onNodeWithText("Done").assertIsSelected()
    }

    @Test
    fun `담당자는 첫 번째 요소가 기본으로 선택된다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf("다이노", "페임스"))

        setContent {
            CreateTaskCardModal(authors = authors)
        }

        onNodeWithText(authors.first()).assertIsSelected()
    }

    @Test
    fun `담당자는 한 항목만 선택 가능하다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf("다이노", "페임스"))

        setContent {
            CreateTaskCardModal(authors = authors)
        }

        onNodeWithText(authors.first()).performClick()
        onNodeWithText(authors.last()).performClick()

        // then

        onNodeWithText(authors.first()).assertIsNotSelected()
        onNodeWithText(authors.last()).assertIsSelected()
    }

    @Test
    fun `제목과 태그가 규칙에 맞게 입력되면 생성 버튼을 누를 수 있다`() = runComposeUiTest {
        setContent {
            CreateTaskCardModal(authors = AuthorGroup(authors = listOf("다이노", "페임스")))
        }
        onNodeWithText("생성").performClick() // 초기 화면은 항상 생성 버튼이 활성회되기 때문에 비활성화 처리를 위해 수행

        onNodeWithText("생성").assertIsNotEnabled()
        onNodeWithText("태스크 제목을 입력하세요").performTextInput("제목")
        onNodeWithText("태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)").performTextInput("   \n태그의 \t,  앞뒤공백은   , 무시  , 됩니다  ")
        onNodeWithText("생성").assertIsEnabled()
    }
}
