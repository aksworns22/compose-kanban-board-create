package woowacourse.kanban.board.ui.creation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.TaskState

@OptIn(ExperimentalTestApi::class)
class CreateTaskCardModalTest {
    val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))

    @Test
    fun `제목을 입력하지 않으면 에러 문구가 노출되고 생성 버튼이 활성화 된다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
            isTitleInitialized = true,
        )
        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("제목을 입력해주세요.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 쉼표로 시작하면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = ",hello",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )
        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 쉼표로 끝나면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "hello,",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `쉼표가 연달아 나오면 형식 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "hello,,hi",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("태그 형식이 올바르지 않습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 5자 이내가 아니라면 태그 규칙 위반 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "123456",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태그가 5개를 초과하면 태그 규칙 위반 에러가 노출되고 생성 버튼이 활성화되지 않는다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "1, 2, 3, 4, 5, 6",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )
        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("태그는 5자 이내로 5개까지만 등록할 수 있습니다.").assertExists()
        onNodeWithText("생성").assertIsNotEnabled()
    }

    @Test
    fun `태스크 상태로 첫 번째 요소가 기본으로 선택된다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("To Do").assertIsSelected()
    }

    @Test
    fun `태스크 상태는 한 항목만 선택 가능하다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("To Do").performClick()
        onNodeWithText("In Progress").performClick()
        onNodeWithText("Done").performClick()

        onNodeWithText("To Do").assertIsNotSelected()
        onNodeWithText("In Progress").assertIsNotSelected()
        onNodeWithText("Done").assertIsSelected()
    }

    @Test
    fun `담당자는 첫 번째 요소가 기본으로 선택된다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText(authors.first().name).assertIsSelected()
    }

    @Test
    fun `담당자는 한 항목만 선택 가능하다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "",
            content = "",
            tags = "",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )

        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText(authors.first().name).performClick()
        onNodeWithText(authors.last().name).performClick()

        onNodeWithText(authors.first().name).assertIsNotSelected()
        onNodeWithText(authors.last().name).assertIsSelected()
    }

    @Test
    fun `제목과 태그가 규칙에 맞게 입력되면 생성 버튼을 누를 수 있다`() = runComposeUiTest {
        val taskCardCreationState = TaskCardCreationState(
            title = "제목",
            content = "",
            tags = "   \n태그의 \t,  앞뒤공백은   , 무시  , 됩니다  ",
            selectedState = TaskState.TO_DO,
            selectedAuthor = authors.first(),
        )
        setContent {
            CreateTaskCardContent(taskCardCreationState = taskCardCreationState, onClose = { }, onCreate = {}, authors = authors)
        }

        onNodeWithText("생성").assertIsEnabled()
    }
}
