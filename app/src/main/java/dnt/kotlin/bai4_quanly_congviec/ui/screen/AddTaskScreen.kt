package dnt.kotlin.bai4_quanly_congviec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dnt.kotlin.bai4_quanly_congviec.data.saveTask
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddJobScreen(navController: NavController) {
    var taskName by remember { mutableStateOf("") }
    var taskDate by remember { mutableStateOf("") }
    var dialogError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

        //TEXTFIELD nhập tên công việc   --------------------------------------------
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Tên Công Việc") },
            modifier = Modifier
                .fillMaxWidth()
        )

        //ROW chọn ngày thực hiện   --------------------------------------------
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            //BUTTON chọn ngày thực hiện   --------------------------------------------
            Button(onClick = {
                showDatePicker(context) { date -> taskDate = date }
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chọn Ngày Thực Hiện")
            }

            //TEXT hiển thị ngày thực hiện   --------------------------------------------
            Spacer(modifier = Modifier.weight(1f))
            if (taskDate.isNotEmpty()) {
                Text(
                    text = "Ngày Thực Hiện: $taskDate",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }

        //BUTTON lưu công việc  ------------------------------------------------
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (taskName.isEmpty() || taskDate.isEmpty()) {
                dialogError = true
            }
            else {
                coroutineScope.launch {
                    for (int in 1..10) { //-> Giả lập thêm 10 bản ghi
                        saveTask(context, taskName, taskDate)
                    }
                    navController.popBackStack()
                }
            }
        }) {
            Text("Lưu Công Việc")

            if(dialogError) showDialog(
                title = "Lỗi",
                content = "Vui lòng nhập đầy đủ thông tin"
            ) { dialogError = false }
        }
    }
}


//===   Hiển thị DatePicker     ===
private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

//===   Hiển thị dialog thông báo   ===
@Composable
private fun showDialog(title: String, content: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(title)
        },
        text = {
            Text(content)
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        },
    )
}
