package woowacourse.kanban.board.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import woowacourse.kanban.board.data.Result
import woowacourse.kanban.board.data.datasource.tasksData
import woowacourse.kanban.board.domain.entity.Task
import woowacourse.kanban.board.domain.repository.TasksRepository

class TasksRepositoryImpl: TasksRepository {
    private val tasks = MutableStateFlow(tasksData)

    override fun createTask(
        title: String,
        content: String,
        tags: List<String>,
        author: String
    ): Result<Unit> {
        return when (val result = Task.of(title, content, tags, author)) {
            is Result.Error -> result
            is Result.Success -> {
                tasks.update { it + result.data }
                Result.Success(Unit)
            }
        }
    }

    override fun getTasks(): Result<List<Task>> = Result.Success(tasks.value)
}