package woowacourse.kanban.board.ui.taskcard

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import org.junit.Test
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.Task

@OptIn(ExperimentalTestApi::class)
class TaskCardTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `모든 필드가 있는 카드 - 제목, 설명, 태그, 담당자 모두 노출`() = runComposeUiTest {
        val task = Task(
            title = "LazyColumn 컴포넌트 구현",
            content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            tags = listOf(Tag("컴포넌트"), Tag("성능")),
            author = "다이노",
        )

        setContent {
            TaskCard(task)
        }

        onNodeWithText("LazyColumn 컴포넌트 구현", useUnmergedTree = true).assertExists()
        onNodeWithText(
            "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            useUnmergedTree = true,
        ).assertExists()
        onNodeWithText("컴포넌트", useUnmergedTree = true).assertExists()
        onNodeWithText("성능", useUnmergedTree = true).assertExists()
        onNodeWithText("다이노", useUnmergedTree = true).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `제목, 태그, 담당자 필드가 있는 카드 - 제목, 태그 담당자 노출`() = runComposeUiTest {
        val task = Task(
            title = "LazyColumn 컴포넌트 구현",
            tags = listOf(Tag("컴포넌트"), Tag("성능")),
            author = "다이노",
        )

        setContent {
            TaskCard(task)
        }

        onNodeWithText("LazyColumn 컴포넌트 구현", useUnmergedTree = true).assertExists()
        onNodeWithText("성능", useUnmergedTree = true).assertExists()
        onNodeWithText("다이노", useUnmergedTree = true).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `제목, 설명, 담당자 필드가 있는 카드 - 제목, 설명, 담당자 노출`() = runComposeUiTest {
        val task = Task(
            title = "LazyColumn 컴포넌트 구현",
            content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            author = "다이노",
        )

        setContent {
            TaskCard(task)
        }

        onNodeWithText("LazyColumn 컴포넌트 구현", useUnmergedTree = true).assertExists()
        onNodeWithText(
            "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
            useUnmergedTree = true,
        ).assertExists()
        onNodeWithText("다이노", useUnmergedTree = true).assertExists()
    }

    @Test
    fun `제목, 담당자 필드가 있는 카드 - 제목, 담당자 노출`() = runComposeUiTest {
        val task = Task(
            title = "LazyColumn 컴포넌트 구현",
            author = "다이노",
        )

        setContent {
            TaskCard(task)
        }

        onNodeWithText("LazyColumn 컴포넌트 구현", useUnmergedTree = true).assertExists()
        onNodeWithText("다이노", useUnmergedTree = true).assertExists()
    }
}
