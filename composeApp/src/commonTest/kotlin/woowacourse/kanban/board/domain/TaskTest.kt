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
        val tags = TagGroup(listOf(Tag("tag1"), Tag("tag2")))
        val author = "author"

        // when
        val task = Task(Title(title), content, tags, TaskState.TO_DO, Author(author))

        // then
        assertThat(task.title.value).isEqualTo(title)
        assertThat(task.content).isEqualTo(content)
        assertThat(task.tags).isEqualTo(TagGroup(listOf(Tag("tag1"), Tag("tag2"))))
        assertThat(task.author.name).isEqualTo(author)
    }

    @Test
    fun `제목이 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        assertThatThrownBy { Task(Title(""), "content", TagGroup(listOf(Tag("tag1"), Tag("tag2"))), TaskState.TO_DO, Author("author")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `담당자가 비어 있거나 공백만 있는 경우 생성이 불가능하다`() {
        assertThatThrownBy { Task(Title("title"), "content", TagGroup(listOf(Tag("tag1"), Tag("tag2"))), TaskState.TO_DO, Author("")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `태그가 5개 초과인 경우 생성이 불가능하다`() {
        val tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5", "tag6")

        assertThatThrownBy { Task(Title("title"), "content", TagGroup(tags.map { Tag(it) }), TaskState.TO_DO, Author("author")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
