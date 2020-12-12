package sheridan.kananid.gymtracker.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class holds the data that we are tracking for each donut: its name, a description, and
 * a rating.
 */
@Entity(tableName = "track")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "height")
    val height: String,
    @ColumnInfo(name = "weight")
    val weight: String,
    @ColumnInfo(name = "excerciseType")
    val excerciseType: String ,

    @ColumnInfo(name = "caloriesBurnt")
    val calories: String,
    @ColumnInfo(name = "distanceTravelled")
    val distance: String,

)
