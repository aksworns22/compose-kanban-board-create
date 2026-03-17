package woowacourse.kanban.board.ui.creation

enum class TitleValidationState {
    INIT,
    VALID,
    EMPTY_ERROR,
}

enum class TagValidationState(val isError: Boolean) {
    VALID(false),
    FORMAT_ERROR(true),
    SIZE_OR_COUNT_ERROR(true),
}
