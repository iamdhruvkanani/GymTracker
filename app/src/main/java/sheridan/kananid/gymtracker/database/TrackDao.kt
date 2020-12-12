package sheridan.kananid.gymtracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The Data Access Object used to retrieve and store data from/to the underlying database.
 * This API is not used directly; instead, callers should use the Repository which calls into
 * this DAO.
 */
@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getAll(): LiveData<List<TrackEntity>>

    @Query("SELECT * FROM track WHERE id = :id")
    suspend fun get(id: Long): TrackEntity

    @Insert
    suspend fun insert(donut: TrackEntity): Long

    @Delete
    suspend fun delete(donut: TrackEntity)

    @Update
    suspend fun update(donut: TrackEntity)
}
