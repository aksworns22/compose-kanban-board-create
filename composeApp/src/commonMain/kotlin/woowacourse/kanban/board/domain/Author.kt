package woowacourse.kanban.board.domain

@JvmInline
value class Author(val name: String) {
    init {
        require(isValid(name)) { "작성자는 공백이나 빈 값일 수 없습니다" }
    }
    companion object {
        fun isValid(value: String): Boolean = value.isNotBlank()
    }
}
