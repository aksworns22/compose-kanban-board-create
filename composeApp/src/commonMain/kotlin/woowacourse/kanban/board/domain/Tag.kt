package woowacourse.kanban.board.domain

data class Tag(val name: String) {
    companion object {
        fun from(name: String): Tag {
            require(name.isNotBlank()) { "태그 내용이 비어 있습니다" }
            require(name.length <= 5) { "태그의 길이는 5자 이하여야 합니다" }
            return Tag(name)
        }

        fun isValid(value: String): Boolean = value.isNotBlank() && value.length <= 5
    }
}
