package woowacourse.kanban.board.domain

data class Task(
    val title: String,
    val content: String = "",
    val tags: List<Tag> = listOf(),
    val taskState: TaskState = TaskState.TO_DO,
    val author: String,
) {
    companion object {
        fun of(title: String, content: String, tagsInput: List<String>, author: String): Task {
            require(isValidTitle(title)) { "제목을 입력해주세요" }
            require(author.isNotBlank()) { "작성자를 입력해주세요" }
            require(isValidTagCount(tagsInput)) { "태그는 최대 5개까지 입력할 수 있습니다" }
            val tags = tagsInput.map { Tag.from(it) }
            return Task(title, content, tags, author = author)
        }

        fun isValidTitle(value: String): Boolean = value.isNotBlank()

        fun isValidTags(tags: List<String>): Boolean = tags.all { Tag.isValid(it) } && isValidTagCount(tags)

        private fun isValidTagCount(tags: List<String>): Boolean = tags.size <= 5
    }
}
