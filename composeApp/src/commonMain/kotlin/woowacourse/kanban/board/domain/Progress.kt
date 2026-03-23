package woowacourse.kanban.board.domain

data class Progress(val total: Int, val completed: Int) {
    companion object {
        fun of(taskGroup: TaskGroup): Progress =
            Progress(total = taskGroup.size, completed = taskGroup.getSameStateTasks(taskState = TaskState.DONE).size)
    }
}
