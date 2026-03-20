package woowacourse.kanban.board.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

class TaskGroupTest {
    @Test
    fun `모든 태스크가 같은 상태라면 올바르게 생성된다`() {
        val taskGroup = TaskGroup(
            type = TaskState.TO_DO,
            tasks = listOf(
                Task(
                    Title("제목1"), "설명1", TagGroup(emptyList()), TaskState.TO_DO,
                    author = Author("붸르"),
                ),
                Task(
                    Title("제목2"), "설명2", TagGroup(emptyList()), TaskState.TO_DO,
                    author = Author("뷁르"),
                ),
            ),
        )

        assertThat(taskGroup.size).isEqualTo(2)
    }

    @Test
    fun `모든 태스크가 같은 상태가 아니라면 예외가 발생한다`() {
        assertThatThrownBy {
            TaskGroup(
                type = TaskState.TO_DO,
                tasks = listOf(
                    Task(
                        Title("제목1"), "설명1", TagGroup(emptyList()), TaskState.TO_DO,
                        author = Author("붸르"),
                    ),
                    Task(
                        Title("제목2"), "설명2", TagGroup(emptyList()), TaskState.IN_PROGRESS,
                        author = Author("뷁르"),
                    ),
                ),
            )
        }.isInstanceOf(IllegalStateException::class.java)
    }
}
