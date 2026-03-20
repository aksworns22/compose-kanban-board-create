package woowacourse.kanban.board.domain

@JvmInline
value class Title(val value: String) {
    init {
        require(isValid(value)) { "제목은 빈 값일 수 없습니다" }
    }

    companion object {
        fun isValid(value: String): Boolean = value.isNotBlank()
    }
}
