package woowacourse.kanban.board.domain

data class Task(val title: Title, val content: String, val tags: TagGroup, val taskState: TaskState = TaskState.TO_DO, val author: Author)

@JvmInline
value class Author(val name: String) {
    init {
        require(isValid(name)) { "작성자는 공백이나 빈 값일 수 없습니다" }
    }
    companion object {
        fun isValid(value: String): Boolean = value.isNotBlank()
    }
}

@JvmInline
value class Title(val value: String) {
    init {
        require(isValid(value)) { "제목은 빈 값일 수 없습니다" }
    }

    companion object {
        fun isValid(value: String): Boolean = value.isNotBlank()
    }
}
