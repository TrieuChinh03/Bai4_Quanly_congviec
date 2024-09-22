package dnt.kotlin.bai4_quanly_congviec.a_preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import dnt.kotlin.bai4_quanly_congviec.data.model.Task
import dnt.kotlin.bai4_quanly_congviec.ui.screen.AddJobScreen
import dnt.kotlin.bai4_quanly_congviec.ui.screen.JobListScreen
import dnt.kotlin.bai4_quanly_congviec.ui.screen.TaskItem

@Preview
@Composable
fun JobListScreenPreview() {
    JobListScreen(navController = NavController(LocalContext.current))
}

@Preview
@Composable
fun AddJobScreenPreview() {
    AddJobScreen(navController = NavController(LocalContext.current))
}
