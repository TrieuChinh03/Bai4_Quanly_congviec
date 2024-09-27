package dnt.kotlin.bai4_quanly_congviec.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import dnt.kotlin.bai4_quanly_congviec.data.dao.TaskDao
import dnt.kotlin.bai4_quanly_congviec.data.model.Task

@Database(entities = [Task::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "data_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

