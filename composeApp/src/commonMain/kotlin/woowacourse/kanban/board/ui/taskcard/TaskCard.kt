package woowacourse.kanban.board.ui.taskcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kanbanboard.composeapp.generated.resources.Res
import kanbanboard.composeapp.generated.resources.profile_image
import org.jetbrains.compose.resources.painterResource
import woowacourse.kanban.board.data.tasksData
import woowacourse.kanban.board.domain.Tag
import woowacourse.kanban.board.domain.Task

private class TaskCardPreviewParameterProvider : PreviewParameterProvider<Task> {
    override val values = tasksData.asSequence()
}

@Composable
@Preview(showBackground = true)
fun TaskCard(@PreviewParameter(TaskCardPreviewParameterProvider::class) task: Task) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.width(286.dp),
    ) {
        Column(
            modifier = Modifier.padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Title(title = task.title)
            if (task.content.isNotEmpty()) Content(content = task.content)
            if (task.tags.isNotEmpty()) Tags(tags = task.tags)
            HorizontalDivider(color = Color(0xFFE5E7EB))
            Profile(author = task.author)
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF101828),
        fontSize = 16.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun Content(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF4A5565),
        fontSize = 14.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun Tags(tags: List<Tag>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .background(Color(0xFFF3F4F6), MaterialTheme.shapes.large)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF364153),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
fun Profile(author: String) {
    Row {
        Image(
            painter = painterResource(Res.drawable.profile_image),
            contentDescription = "Profile Image",
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = author,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF364153),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
