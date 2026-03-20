package woowacourse.kanban.board.domain

data class Task(val title: Title, val content: String, val tags: TagGroup, val taskState: TaskState = TaskState.TO_DO, val author: Author) {
    fun changeState(taskState: TaskState): Task = Task(title, content, tags, taskState, author)
}
