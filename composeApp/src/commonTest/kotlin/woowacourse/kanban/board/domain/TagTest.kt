package woowacourse.kanban.board.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class TagTest {

    @Test
    fun `태그 생성 성공 테스트`() {
        // given
        val name = "tag"

        // when
        val tag = Tag(name)

        // then
        assertThat(tag.name).isEqualTo(name)
    }

    @Test
    fun `태스크 내용이 비어 있는 경우 생성이 불가능하다`() {
        assertThatThrownBy { Tag("") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `태그의 길이가 5자를 초과할 경우 생성이 불가능하다`() {
        assertThatThrownBy { Tag("abcedf") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
