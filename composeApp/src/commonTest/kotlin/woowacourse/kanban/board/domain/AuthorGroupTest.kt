package woowacourse.kanban.board.domain

import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class AuthorGroupTest {
    @Test
    fun `담당자가 한 명도 없다면 예외가 발생한다`() {
        assertThatThrownBy {
            AuthorGroup(authors = emptyList())
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `담당자 그룹은 담당자가 한 명이상 들어오면 생성할 수 있다`() {
        assertThat(AuthorGroup(authors = listOf(Author("베르"))).size).isEqualTo(1)
    }
}
