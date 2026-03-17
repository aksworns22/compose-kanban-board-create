package woowacourse.kanban.board.domain

data class Tag(val name: String) {
    init {
        require(isValid(name)) { "태그 내용은 빈 값이나 공백일 수 없으며 5자 이하여야 합니다" }
    }
    companion object {
        fun isValid(value: String): Boolean = value.isNotBlank() && value.length <= 5
    }
}
