package woowacourse.kanban.board.domain

@JvmInline
value class TagGroup(val tags: List<Tag>) {
    init {
        require(isValid(tags)) { "태그는 5개 이하여야 합니다" }
    }

    val isEmpty: Boolean
        get() = tags.isEmpty()

    companion object {
        fun isValid(tags: List<Tag>): Boolean = tags.size <= 5
    }
}
