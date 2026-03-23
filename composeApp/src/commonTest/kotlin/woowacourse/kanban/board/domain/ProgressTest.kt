package woowacourse.kanban.board.domain

import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat

class ProgressTest {
    @Test
    fun `작업 완료 상태와 나머지 상태를 기준으로 완료율을 계산한다`() {
        val taskGroup = TaskGroup(
            setOf(
                Task(
                    title = Title("해야할 일 제목"),
                    content = "해야할 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("해야할일"))),
                    taskState = TaskState.TO_DO,
                    author = Author("뷁르"),
                ),
                Task(
                    title = Title("진행중인 일 제목"),
                    content = "진행중인 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("진행중인일"))),
                    taskState = TaskState.IN_PROGRESS,
                    author = Author("벨루"),

                ),
                Task(
                    title = Title("끝난 일 제목"),
                    content = "끝난 일 내용",
                    tags = TagGroup(tags = listOf(Tag("멋진"), Tag("끝난일"))),
                    taskState = TaskState.DONE,
                    author = Author("베르르르르르"),
                ),
            ),
        )
        assertThat(Progress.of(taskGroup)).isEqualTo(Progress(total = 3, completed = 1))
    }
}
