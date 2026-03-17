package woowacourse.kanban.board.domain

data class AuthorGroup(private val authors: List<String>) {
    init {
        require(authors.isNotEmpty()) { "작업 담당자는 한 명 이상 있어야 합니다" }
    }
    val size: Int
        get() = authors.size

    operator fun get(index: Int): String = authors[index]
    fun first(): String = authors.first()
    fun last(): String = authors.last()
}
