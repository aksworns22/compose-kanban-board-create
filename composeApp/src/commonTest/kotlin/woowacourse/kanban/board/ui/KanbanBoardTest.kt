package woowacourse.kanban.board.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertValueEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.board.domain.Author
import woowacourse.kanban.board.domain.AuthorGroup
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.TagGroup
import woowacourse.kanban.board.domain.Task
import woowacourse.kanban.board.domain.TaskState
import woowacourse.kanban.board.domain.Title
import woowacourse.kanban.board.ui.creation.CreateTaskCardScreen
import woowacourse.kanban.board.ui.creation.TaskCardCreationState

@OptIn(ExperimentalTestApi::class)
class KanbanBoardTest {
    @Test
    fun `새 태스크 생성 버튼을 누르면 생성 모달(다이어로그)를 표시한다`() = runComposeUiTest {
        val kanbanBoardState = KanbanBoardState(isDialogOpened = true)
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("새 태스크 생성").isDisplayed()
    }

    @Test
    fun `x 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = true) }
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("x 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }

    @Test
    fun `취소 버튼을 선택하면 태스크 생성 모달(다이어로그)가 사라진다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        setContent {
            val kanbanBoardState = remember { KanbanBoardState(isDialogOpened = true) }
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(selectedAuthor = authors.first()),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { },
                    )
                },
            )
        }
        onNodeWithContentDescription("취소 버튼").performClick()
        onNodeWithContentDescription("새 태스크 생성").assertDoesNotExist()
    }

    @Test
    fun `제목이 존재하고 태그의 형식, 상태가 올바르면 새로운 태스크를 생성한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("디이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(isDialogOpened = true)
        setContent {
            KanbanBoardContent(
                kanbanBoardState = kanbanBoardState,
                dialogScreen = {
                    CreateTaskCardScreen(
                        authors = authors,
                        taskCardCreationState = TaskCardCreationState(
                            title = "멋진 제목",
                            content = "멋진 내용",
                            tags = "멋진, 태그",
                            selectedAuthor = authors.first(),
                            isTitleInitialized = true,
                        ),
                        onClose = { kanbanBoardState.isDialogOpened = false },
                        onCreate = { task ->
                            kanbanBoardState.taskTransitionSnapshot =
                                kanbanBoardState.taskTransitionSnapshot.transition(task, task.taskState)
                            kanbanBoardState.isDialogOpened = false
                        },
                    )
                },
            )
        }
        onNodeWithContentDescription("새 태스크 생성 버튼").performClick()
        val expectedTaskTransitionSnapshot = TaskTransitionSnapshot(
            tasks = mapOf(
                TaskState.TO_DO to TaskGroup(
                    type = TaskState.TO_DO,
                    tasks = listOf(
                        Task(
                            title = Title("멋진 제목"),
                            content = "멋진 내용",
                            tags = TagGroup(tags = listOf(Tag("멋진"), Tag("태그"))),
                            taskState = TaskState.TO_DO,
                            author = authors.first(),
                        ),
                    ),
                ),
                TaskState.IN_PROGRESS to TaskGroup(
                    type = TaskState.IN_PROGRESS,
                    tasks = emptyList(),
                ),
                TaskState.DONE to TaskGroup(
                    type = TaskState.DONE,
                    tasks = emptyList(),
                ),
            ),
        )
        assertThat(kanbanBoardState.taskTransitionSnapshot).isEqualTo(expectedTaskTransitionSnapshot)
    }

    @Test
    fun `생성된 태스크를 각 태스크 상태에 맞게 표시한다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(
            isDialogOpened = false,
            taskTransitionSnapshot = TaskTransitionSnapshot(
                mapOf(
                    TaskState.TO_DO to TaskGroup(
                        type = TaskState.TO_DO,
                        tasks = listOf(
                            Task(
                                title = Title("해야할 일 제목"),
                                content = "해야할 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                                taskState = TaskState.TO_DO,
                                author = authors.first(),
                            ),
                        ),
                    ),
                    TaskState.IN_PROGRESS to TaskGroup(
                        type = TaskState.IN_PROGRESS,
                        tasks = listOf(
                            Task(
                                title = Title("진행중인 일 제목"),
                                content = "진행중인 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("진행중인일"))),
                                taskState = TaskState.IN_PROGRESS,
                                author = authors.first(),
                            ),
                        ),
                    ),
                    TaskState.DONE to TaskGroup(
                        type = TaskState.DONE,
                        tasks = listOf(
                            Task(
                                title = Title("끝난 일 제목"),
                                content = "끝난 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("끝난일"))),
                                taskState = TaskState.DONE,
                                author = authors.last(),
                            ),
                        ),
                    ),
                ),
            ),
        )

        setContent {
            KanbanBoardContent(kanbanBoardState = kanbanBoardState, dialogScreen = { })
        }

        onNodeWithContentDescription("To do 목록")
            .onChildren()[0].assertContentDescriptionEquals("해야할 일 제목에 대한 태스크 카드")
        onNodeWithContentDescription("In progress 목록")
            .onChildren()[0].assertContentDescriptionEquals("진행중인 일 제목에 대한 태스크 카드")
        onNodeWithContentDescription("Done 목록")
            .onChildren()[0].assertContentDescriptionEquals("끝난 일 제목에 대한 태스크 카드")
    }

    @Test
    fun `각 상태에 따른 태스크 카드의 개수에 따라 올바른 숫자가 표시된다`() = runComposeUiTest {
        val authors = AuthorGroup(authors = listOf(Author("다이노"), Author("페임스")))
        val kanbanBoardState = KanbanBoardState(
            isDialogOpened = false,
            taskTransitionSnapshot = TaskTransitionSnapshot(
                mapOf(
                    TaskState.TO_DO to TaskGroup(
                        type = TaskState.TO_DO,
                        tasks = listOf(
                            Task(
                                title = Title("해야할 일 제목 1"),
                                content = "해야할 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                                taskState = TaskState.TO_DO,
                                author = authors.first(),
                            ),
                            Task(
                                title = Title("해야할 일 제목 2"),
                                content = "해야할 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                                taskState = TaskState.TO_DO,
                                author = authors.first(),
                            ),
                        ),
                    ),
                    TaskState.IN_PROGRESS to TaskGroup(
                        type = TaskState.IN_PROGRESS,
                        tasks = listOf(
                            Task(
                                title = Title("진행중인 일 제목 1"),
                                content = "진행중인 일 내용",
                                tags = TagGroup(tags = listOf(Tag("멋진"), Tag("진행중인일"))),
                                taskState = TaskState.IN_PROGRESS,
                                author = authors.first(),
                            ),
                        ),
                    ),
                    TaskState.DONE to TaskGroup(
                        type = TaskState.DONE,
                        tasks = emptyList(),
                    ),
                ),
            ),
        )

        setContent {
            KanbanBoardContent(kanbanBoardState = kanbanBoardState, dialogScreen = { })
        }

        onNodeWithContentDescription("To do 태스크 가드 개수").assertTextEquals("2")
        onNodeWithContentDescription("In progress 태스크 가드 개수").assertTextEquals("1")
        onNodeWithContentDescription("Done 태스크 가드 개수").assertTextEquals("0")
    }
}
