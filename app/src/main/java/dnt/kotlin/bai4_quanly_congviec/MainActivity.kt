package dnt.kotlin.bai4_quanly_congviec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dnt.kotlin.bai4_quanly_congviec.ui.screen.AddTaskScreen
import dnt.kotlin.bai4_quanly_congviec.ui.screen.Screens
import dnt.kotlin.bai4_quanly_congviec.ui.screen.TaskListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController, startDestination = Screens.TASK_LIST) {
                    composable(Screens.TASK_LIST) { TaskListScreen(navController) }
                    composable(Screens.ADD_TASK) { AddTaskScreen(navController) }
                }
            }
        }
    }
}
