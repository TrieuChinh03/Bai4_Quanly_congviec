package dnt.kotlin.bai4_quanly_congviec.data

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.widget.Toast
import dnt.kotlin.bai4_quanly_congviec.data.database.AppDatabase
import dnt.kotlin.bai4_quanly_congviec.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobContentProvider : ContentProvider() {
    private lateinit var db: AppDatabase

    companion object {
        private const val AUTHORITY = "com.example.jobapp.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/tasks")
        const val TASK = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "tasks", TASK)
        }
    }

    override fun onCreate(): Boolean {
        db = AppDatabase.getDatabase(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "name", "date"))
        if (uriMatcher.match(uri) == TASK) {
            val jobs = db.taskDao().getAllTasks()
            for (job in jobs) {
                cursor.addRow(arrayOf(job.id, job.name, job.date))
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val job = Task(
            name = values?.getAsString("name") ?: "",
            date = values?.getAsString("date") ?: ""
        )
        db.taskDao().insert(job)
        return Uri.withAppendedPath(CONTENT_URI, job.id.toString())
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun getType(uri: Uri): String? = null
}

//===   Load dữ liệu    ===
@SuppressLint("Range")
suspend fun loadTasks(contentResolver: ContentResolver, tasks: MutableList<Task>) {
    withContext(Dispatchers.IO) {
        val cursor: Cursor? = contentResolver.query(JobContentProvider.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val task = Task(
                    id = it.getLong(it.getColumnIndex("id")),
                    name = it.getString(it.getColumnIndex("name")),
                    date = it.getString(it.getColumnIndex("date"))
                )
                tasks.add(task)
            }
        }
    }
}

//===   Lưu công việc    ===
suspend fun saveTask(context: Context, taskName: String, taskDate: String) {
    withContext(Dispatchers.IO) {
        if (taskName.isNotEmpty() && taskDate.isNotEmpty()) {
            val contentValues = ContentValues().apply {
                put("name", taskName)
                put("date", taskDate)
            }
            context.contentResolver.insert(JobContentProvider.CONTENT_URI, contentValues)
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
