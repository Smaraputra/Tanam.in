package id.capstone.tanamin.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Classes::class,Moduls::class,Informations::class],version=1)
abstract class TanaminRoomDatabase : RoomDatabase() {
    abstract fun tanaminDao(): TanaminDao
    companion object {
        @Volatile
        private var INSTANCE: TanaminRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): TanaminRoomDatabase {
            if (INSTANCE == null) {
                synchronized(TanaminRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        TanaminRoomDatabase::class.java, "tanamin_database").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as TanaminRoomDatabase
        }
    }
}