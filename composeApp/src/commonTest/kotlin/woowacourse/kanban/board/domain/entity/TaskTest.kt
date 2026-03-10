package woowacourse.kanban.board.domain.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.kanban.board.data.Result

class TaskTest {

    @Test
    fun `태스크 생성 성공 테스트`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2")
        val author = "author"

        // when
        val result = Task.of(title, content, tags, author)

        // then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(result as Result.Success).extracting("data.title").isEqualTo("title")
        assertThat(result.data.content).isEqualTo("content")
        assertThat(result.data.tags).isEqualTo(listOf(Tag("tag1"), Tag("tag2")))
        assertThat(result.data.author).isEqualTo("author")
    }

    @Test
    fun `제목이 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        // given
        val title = ""

        // when
        val result = Task.of(title, "content", listOf("tag1", "tag2"), "author")

        // then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result as Result.Error).extracting("exception.message").isEqualTo("제목을 입력해주세요")
    }

    @Test
    fun `담당자가 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        // given
        val author = ""

        // when
        val result = Task.of("title", "content", listOf("tag1", "tag2"), author)

        // then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result as Result.Error).extracting("exception.message").isEqualTo("작성자를 입력해주세요")
    }

    @Test
    fun `태그가 5개 초과인 경우 생성이 불가능하다`() {
        // given
        val tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5", "tag6")

        // when
        val result = Task.of("title", "content", tags, "author")

        // then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result as Result.Error).extracting("exception.message").isEqualTo("태그는 최대 5개까지 입력할 수 있습니다")
    }
}