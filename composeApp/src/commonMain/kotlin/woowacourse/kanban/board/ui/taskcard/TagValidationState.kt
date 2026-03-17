package woowacourse.kanban.board.ui.taskcard

enum class TagValidationState(val isError: Boolean) {
    VALID(false),
    FORMAT_ERROR(true),
    SIZE_OR_COUNT_ERROR(true),
}
