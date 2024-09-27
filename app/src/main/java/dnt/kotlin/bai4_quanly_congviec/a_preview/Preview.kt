package dnt.kotlin.bai4_quanly_congviec.a_preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import dnt.kotlin.bai4_quanly_congviec.ui.screen.AddTaskScreen
import dnt.kotlin.bai4_quanly_congviec.ui.screen.TaskListScreen

@Preview
@Composable
fun JobListScreenPreview() {
    TaskListScreen(navController = NavController(LocalContext.current))
}

@Preview
@Composable
fun AddJobScreenPreview() {
    AddTaskScreen(navController = NavController(LocalContext.current))
}
