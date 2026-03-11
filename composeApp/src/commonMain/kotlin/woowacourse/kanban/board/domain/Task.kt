package woowacourse.kanban.board.domain

data class Task(
    val title: String,
    val content: String = "",
    val tags: List<Tag> = listOf(),
    val author: String
) {
    companion object {
        fun of(title: String, content: String, tagsInput: List<String>, author: String): Task {
            require(isValidTitle(title)) { "제목을 입력해주세요" }
            require(author.isNotBlank()) { "작성자를 입력해주세요" }
            require(tagsInput.size <= 5) { "태그는 최대 5개까지 입력할 수 있습니다" }
            val tags = tagsInput.map { Tag.from(it) }
            return Task(title, content, tags, author)
        }

        fun isValidTitle(value: String): Boolean = value.isNotBlank()
    }
}