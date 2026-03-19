package woowacourse.kanban.board.ui.creation

enum class TitleValidationState(val isError: Boolean) {
    INIT(true),
    VALID(false),
    EMPTY_ERROR(true),
}

enum class TagValidationState(val isError: Boolean) {
    VALID(false),
    FORMAT_ERROR(true),
    SIZE_OR_COUNT_ERROR(true),
}
