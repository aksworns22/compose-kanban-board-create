package woowacourse.kanban.board.domain.entity

import woowacourse.kanban.board.data.Result

data class Task(
    val title: String,
    val content: String = "",
    val tags: List<Tag> = listOf(),
    val author: String
) {
    companion object {
        fun of(title: String, content: String, tagsInput: List<String>, author: String): Result<Task> {
            if (title.isBlank()) return Result.Error(Exception("제목을 입력해주세요"))
            if (author.isBlank()) return Result.Error(Exception("작성자를 입력해주세요"))
            if (tagsInput.size > 5) return Result.Error(Exception("태그는 최대 5개까지 입력할 수 있습니다"))
            val tags = tagsInput.map {
                val result = Tag.from(it)
                if (result is Result.Error) return result
                result as Result.Success
                result.data
            }
            return Result.Success(Task(title, content, tags, author))
        }
    }
}