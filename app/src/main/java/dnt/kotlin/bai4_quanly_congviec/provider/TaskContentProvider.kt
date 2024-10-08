package dnt.kotlin.bai4_quanly_congviec.provider

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import dnt.kotlin.bai4_quanly_congviec.data.database.AppDatabase
import dnt.kotlin.bai4_quanly_congviec.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskContentProvider : ContentProvider() {
    private lateinit var db: AppDatabase

    companion object {
        private const val AUTHORITY = "dnt.kotlin.bai4_quanly_congviec.provider"
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

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val cursor = MatrixCursor(arrayOf("id", "title", "content", "date"))
        if (uriMatcher.match(uri) == TASK) {
            val tasks = db.taskDao().getAllTasks()
            for (task in tasks) {
                cursor.addRow(arrayOf(task.id, task.title, task.content, task.date))
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val task = Task(
            id = 0,
            title = values?.getAsString("title") ?: "",
            content = values?.getAsString("content") ?: "",
            date = values?.getAsString("date") ?: ""
        )
        db.taskDao().insert(task)
        return Uri.withAppendedPath(CONTENT_URI, task.id.toString())
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun getType(uri: Uri): String? = null
}

//===   Load dữ liệu    ===
@SuppressLint("Range")
suspend fun loadTasks(contentResolver: ContentResolver, tasks: MutableList<Task>) {
    withContext(Dispatchers.IO) {
        val cursor: Cursor? = contentResolver.query(TaskContentProvider.CONTENT_URI, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val task = Task(
                    id = it.getLong(it.getColumnIndex("id")),
                    title = it.getString(it.getColumnIndex("title")),
                    content = it.getString(it.getColumnIndex("content")),
                    date = it.getString(it.getColumnIndex("date"))
                )
                tasks.add(task)
            }
        }
    }
}

//===   Lưu công việc    ===
suspend fun saveTask(context: Context, task: Task) {
    withContext(Dispatchers.IO) {
        val contentValues = ContentValues().apply {
            put("title", task.title)
            put("content", task.content)
            put("date", task.date)
        }
        context.contentResolver.insert(TaskContentProvider.CONTENT_URI, contentValues)
    }
}

