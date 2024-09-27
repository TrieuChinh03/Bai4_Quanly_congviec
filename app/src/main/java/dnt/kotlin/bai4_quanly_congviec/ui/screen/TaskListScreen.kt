package dnt.kotlin.bai4_quanly_congviec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.ContentResolver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import dnt.kotlin.bai4_quanly_congviec.data.loadTasks
import dnt.kotlin.bai4_quanly_congviec.data.model.Task
import dnt.kotlin.bai4_quanly_congviec.ui.theme.Background_Item
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter

@Preview
@Composable
private fun TaskListScreenPreview() {
    TaskListScreen(navController = NavController(LocalContext.current))
}

@Preview
@Composable
private fun TaskItemPreview() {
    TaskItem(task = Task(2, "Công việc 1", "21/09/2023"))
}

@Composable
fun TaskListScreen(navController: NavController) {
    val tasks = remember { mutableStateListOf<Task>() }
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    var current = -1

    LaunchedEffect(Unit) {
        loadTasks(contentResolver, tasks)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        //LAZYCOLUMN danh sách công việc ----------------------------------------------------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            itemsIndexed(tasks) { index, task ->
                if(current < index) {
                    current = index
                    AdapterTast(task = task, animation = true, delay = 50L * index)
                }
                else {
                    AdapterTast(task = task, animation = false, delay = 0)
                }
            }
        }

        //BUTTON thêm công việc  -----------------------------------------------------------
        FloatingActionButton(
            onClick = { navController.navigate(Screens.ADD_TASK) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Add,
                contentDescription = "add task")
        }
    }

}

/**     Adapter Item    **/
@Composable
fun AdapterTast(task: Task, animation: Boolean, delay: Long) {
    if(animation) {
        var isVisible by remember(task.id) { mutableStateOf(false) }
        LaunchedEffect(key1 = task.id) {
            delay(delay)
            isVisible = true
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 500)),
        ) {
            TaskItem(task)
        }
    }
    else {
        TaskItem(task)
    }
}

/**  Item công việc   **/
@Composable
fun TaskItem(task: Task) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(Background_Item)
                .padding(12.dp)
        ) {
            //---   Tên công việc   ---
            task.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            //---   Ngày thực hiện   ---
            Spacer(modifier = Modifier.height(8.dp))
            task.date?.let {
                Text(
                    text = it.format(DateTimeFormatter.ofPattern("dd 'ngày' MM 'tháng' yyyy")),
                )
            }
        }
    }
}