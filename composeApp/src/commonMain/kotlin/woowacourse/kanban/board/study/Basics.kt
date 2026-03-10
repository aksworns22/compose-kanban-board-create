package woowacourse.kanban.board.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kanbanboard.composeapp.generated.resources.Res
import kanbanboard.composeapp.generated.resources.woowa
import org.jetbrains.compose.resources.painterResource

@Composable
fun CheckerScreen() {
    var checked by remember { mutableStateOf(true) }
    CheckerView(checked = checked) {
        checked = !checked
    }
}

@Composable
fun CheckerView(checked: Boolean, check: () -> Unit) {
    Column {
        Checkbox(
            checked = checked,
            onCheckedChange = { check() },
        )
        if (checked) Text(text = "체크됨!!!")
    }
}

@Composable
fun TextExample() {
    Text(
        text = "Jetpack Compose Text 실습",
        color = Color.Blue,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ImageExample() {
    Image(
        painter = painterResource(Res.drawable.woowa),
        contentDescription = "우아한테크코스"
    )
}

@Composable
fun ImageExample2() {
    Icon(Icons.Default.Favorite, contentDescription = "Favorite")
}

@Composable
fun ButtonExample() {
    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
        Text(text = "저장", color = Color.White)
    }
}

@Composable
fun SaveButton() {
    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
        Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.Black)
    }
}

@Composable
fun ColumnExample() {
    Column {
        Text("첫 번째 아이템")
        Text("두 번째 아이템")
        Text("세 번째 아이템")
    }
}

@Composable
fun RowExample() {
    Row {
        Text("첫 번째")
        Text("두 번째")
        Text("세 번째")
    }
}

@Composable
fun FavoriteButton() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.Black)
            Text(text = "좋아요", color = Color.White)
        }
    }
}

@Composable
fun SimpleBox() {
    Box(
        modifier = Modifier.size(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
                .align(Alignment.TopStart)
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Green)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun SimpleBox2() {
    Box(
        modifier = Modifier.size(200.dp)
    ) {
        Box(modifier = Modifier
            .size(50.dp)
            .background(Color.Red)
            .align(Alignment.TopStart)
        )

        Box(modifier = Modifier
            .size(50.dp)
            .background(Color.Yellow)
            .align(Alignment.Center)
        )

        Box(modifier = Modifier
            .size(50.dp)
            .background(Color.Green)
            .align(Alignment.BottomEnd))
    }
}