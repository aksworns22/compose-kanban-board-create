package woowacourse.kanban.board.domain

@JvmInline
value class AuthorGroup(private val authors: List<Author>) {
    init {
        require(authors.isNotEmpty()) { "작업 담당자는 한 명 이상 있어야 합니다" }
    }
    val size: Int
        get() = authors.size

    operator fun get(index: Int): Author = authors[index]
    fun first(): Author = authors.first()
    fun last(): Author = authors.last()
}
