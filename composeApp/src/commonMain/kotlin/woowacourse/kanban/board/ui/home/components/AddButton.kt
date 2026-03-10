package woowacourse.kanban.board.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kanbanboard.composeapp.generated.resources.Res
import kanbanboard.composeapp.generated.resources.task_planet
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddButton(
    modifier: Modifier,
    onValueChange: () -> Unit,
) {
    Button(
        onClick = { onValueChange() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5CFFD1),
            contentColor = Color.Black,
        ),
        modifier = modifier
    ) {
        Row {
            Image(
                painter = painterResource(Res.drawable.task_planet),
                contentDescription = "행성이 이미지",
                modifier = Modifier.size(50.dp),
            )
            Text(
                text = "추가",
                fontSize = 50.sp,
            )
        }
    }
}

@Preview
@Composable
private fun AddButtonPreview() {
    AddButton(modifier = Modifier, onValueChange = {})
}