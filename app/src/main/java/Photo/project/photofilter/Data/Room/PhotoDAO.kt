package Photo.project.photofilter.Data.Room

import Photo.project.photofilter.Constants.DATABASENAME
import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.PhotoFilter
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDAO {

    @Insert
    suspend fun savePhoto(photoItem: PhotoItem)


    @Delete
    suspend fun deletePhoto(photoItem: PhotoItem)

    @Query("SELECT * FROM PhotoItem")
    fun getPhotos() : Flow<List<PhotoItem>>
}



@Database(entities = [PhotoItem::class], version = 1)
abstract class PhotoDatabase() : RoomDatabase(){
    abstract fun getDAO() : PhotoDAO

    companion object{

        fun createDatabase() : PhotoDatabase{
            return Room.databaseBuilder(
                PhotoFilter.context,
                PhotoDatabase::class.java,
                DATABASENAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}