package woowacourse.kanban.board.domain.repository

import woowacourse.kanban.board.data.Result
import woowacourse.kanban.board.domain.entity.Task

interface TasksRepository {
    fun createTask(
        title: String,
        content: String,
        tags: List<String>,
        author: String
    ): Result<Unit>
    fun getTasks(): Result<List<Task>>
}