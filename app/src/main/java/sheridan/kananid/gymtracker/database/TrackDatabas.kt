package sheridan.kananid.gymtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class], version = 1)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile private var INSTANCE: TrackDatabase? = null

        fun getDatabase(context: Context): TrackDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TrackDatabase::class.java,
                    "track_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
