package woowacourse.kanban.board.data

import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.Task

val tasksData = listOf(
    Task(
        title = "LazyColumn 컴포넌트 구현",
        content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
        tags = listOf(Tag("컴포넌트"), Tag("성능")),
        author = "다이노",
    ),
    Task(
        title = "LazyColumn 컴포넌트 구현",
        tags = listOf(Tag("컴포넌트"), Tag("성능")),
        author = "다이노",
    ),
    Task(
        title = "LazyColumn 컴포넌트 구현",
        content = "세로 스크롤 가능한 리스트 컴포넌트를 만들고 성능 최적화를 적용합니다.",
        author = "다이노",
    ),
    Task(
        title = "LazyColumn 컴포넌트 구현",
        author = "다이노",
    ),
    Task(
        title = "너무너무 긴 제목은 한 줄까지만 노출되고 말줄임표로 처리합니다",
        content = "너무너무너무 긴 설명은 두 줄까지만 노출하고 말줄임표로 처리합니다 두 줄까지만 노출하고 말줄임표로 처리합니다",
        tags = listOf(Tag("너무너무"), Tag("긴 태그"), Tag("최대로"), Tag("5자까지"), Tag("5개제한임")),
        author = "너무너무너무 긴 담당자도 한 줄이지만 노출되고 말줄임표로 처리합니다",
    ),
)
