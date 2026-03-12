package woowacourse.kanban.board.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class TaskTest {

    @Test
    fun `태스크 생성 성공 테스트`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2")
        val author = "author"

        // when
        val task = Task.of(title, content, tags, author)

        // then
        assertThat(task.title).isEqualTo(title)
        assertThat(task.content).isEqualTo(content)
        assertThat(task.tags).isEqualTo(listOf(Tag("tag1"), Tag("tag2")))
        assertThat(task.author).isEqualTo(author)
    }

    @Test
    fun `제목이 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        assertThatThrownBy { Task.of("", "content", listOf("tag1", "tag2"), "author") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("제목을 입력해주세요")
    }

    @Test
    fun `담당자가 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        assertThatThrownBy { Task.of("title", "content", listOf("tag1", "tag2"), "") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("작성자를 입력해주세요")
    }

    @Test
    fun `태그가 5개 초과인 경우 생성이 불가능하다`() {
        val tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5", "tag6")

        assertThatThrownBy { Task.of("title", "content", tags, "author") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("태그는 최대 5개까지 입력할 수 있습니다")
    }
}
