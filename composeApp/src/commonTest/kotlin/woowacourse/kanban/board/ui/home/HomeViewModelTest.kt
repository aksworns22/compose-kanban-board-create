package woowacourse.kanban.board.ui.home

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import woowacourse.kanban.board.data.repository.TasksRepositoryImpl
import woowacourse.kanban.board.domain.entity.Tag
import woowacourse.kanban.board.domain.repository.TasksRepository
import kotlin.test.Test

class HomeViewModelTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        tasksRepository = TasksRepositoryImpl()
        viewModel = HomeViewModel(tasksRepository)
    }

    @Test
    fun `태스크 목록 가져오기 성공 테스트`() {
        // when
        viewModel.getTasks()
        val tasks = viewModel.uiState.value.tasks

        // then
        assertThat(tasks.size).isEqualTo(5)
        assertThat(tasks.first().title).isEqualTo("LazyColumn 컴포넌트 구현")
    }

    @Test
    fun `태스크 생성 성공 테스트`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2")
        val author = "author"

        // when
        viewModel.addTask(title, content, tags, author)
        val tasks = viewModel.uiState.value.tasks

        // then
        assertThat(tasks.size).isEqualTo(6)
        assertThat(tasks.last().title).isEqualTo("title")
        assertThat(tasks.last().content).isEqualTo("content")
        assertThat(tasks.last().tags).isEqualTo(listOf(Tag("tag1"), Tag("tag2")))
        assertThat(tasks.last().author).isEqualTo("author")
    }

    @Test
    fun `태스크 생성 실패 시 에러 메시지를 표시한다 - 제목 빈 값`() {
        // given
        val title = ""
        val content = "content"
        val tags = listOf("tag1", "tag2")
        val author = "author"

        // when
        viewModel.addTask(title, content, tags, author)
        val errorMessage = viewModel.uiState.value.errorMessage

        // then
        assertThat(errorMessage).isEqualTo("제목을 입력해주세요")
    }

    @Test
    fun `태스크 생성 실패 시 에러 메시지를 표시한다 - 작성자 빈 값`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2")
        val author = ""

        // when
        viewModel.addTask(title, content, tags, author)
        val errorMessage = viewModel.uiState.value.errorMessage

        // then
        assertThat(errorMessage).isEqualTo("작성자를 입력해주세요")
    }

    @Test
    fun `태스크 생성 실패 시 에러 메시지를 표시한다 - 태그의 개수가 5개 초과`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5", "tag6")
        val author = "author"

        // when
        viewModel.addTask(title, content, tags, author)
        val errorMessage = viewModel.uiState.value.errorMessage

        // then
        assertThat(errorMessage).isEqualTo("태그는 최대 5개까지 입력할 수 있습니다")
    }

    @Test
    fun `태스크 생성 실패 시 에러 메시지를 표시한다 - 태그 내용의 길이가 5자 초과`() {
        // given
        val title = "title"
        val content = "content"
        val tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5678")
        val author = "author"

        // when
        viewModel.addTask(title, content, tags, author)
        val errorMessage = viewModel.uiState.value.errorMessage

        // then
        assertThat(errorMessage).isEqualTo("태그의 길이는 5자 이하여야 합니다")
    }
}