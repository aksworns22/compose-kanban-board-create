package woowacourse.kanban.board.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import woowacourse.kanban.board.data.Result
import woowacourse.kanban.board.data.repository.TasksRepositoryImpl
import woowacourse.kanban.board.domain.entity.Task
import woowacourse.kanban.board.domain.repository.TasksRepository

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val errorMessage: String = ""
)

class HomeViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
       refresh()
    }

    fun refresh() {
        clearErrorMessage()
        getTasks()
    }

    fun getTasks() {
        val result = tasksRepository.getTasks()
        if (result is Result.Success) {
            _uiState.update { it.copy(tasks = result.data) }
        }
    }

    fun addTask(title: String, content: String, tags: List<String>, author: String): Boolean {
        when (val result = tasksRepository.createTask(title, content, tags, author)) {
            is Result.Success -> {
                refresh()
                return true
            }
            is Result.Error -> {
                _uiState.update { it.copy(errorMessage = result.exception.message.toString()) }
                return false
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = "") }
    }
}

val homeViewModelFactory = viewModelFactory {
    initializer {
        HomeViewModel(tasksRepository = getTasksRepository())
    }
}

private fun getTasksRepository(): TasksRepository = TasksRepositoryImpl()