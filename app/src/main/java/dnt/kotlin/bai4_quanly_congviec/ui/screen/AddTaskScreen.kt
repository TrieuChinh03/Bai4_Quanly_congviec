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
import dnt.kotlin.bai4_quanly_congviec.data.model.Task
import dnt.kotlin.bai4_quanly_congviec.provider.saveTask
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AddTaskScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    var dialogError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

        //TEXTFIELD nhập tiêu đề công việc   --------------------------------------------
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tiêu đề") },
            modifier = Modifier
                .fillMaxWidth()
        )

        //TEXTFIELD nhập nội dung công việc   --------------------------------------------
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Nội dung") },
            modifier = Modifier
                .fillMaxWidth()
        )

        //ROW chọn ngày thực hiện   --------------------------------------------
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            //BUTTON chọn ngày thực hiện   --------------------------------------------
            Button(onClick = {
                showDatePicker(context) { dateSelected -> date = dateSelected }
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.size(24.dp)
                )
            }

            //TEXT hiển thị ngày thực hiện   --------------------------------------------
            Spacer(modifier = Modifier.weight(1f))
            if (date.isNotEmpty()) {
                Text(
                    text = "Ngày: $date",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }

        //BUTTON lưu công việc  ------------------------------------------------
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (title.isEmpty() || content.isEmpty() || date.isEmpty()) {
                dialogError = true
            }
            else {
                coroutineScope.launch {
                    saveTask(context, Task(0, title, content, date))
                    navController.popBackStack()
                }
            }
        }) {
            Text("Lưu")

            if(dialogError) DialogError { dialogError = false }
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
private fun DialogError(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text("Vui lòng nhập đầy đủ thông tin")
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        },
    )
}
