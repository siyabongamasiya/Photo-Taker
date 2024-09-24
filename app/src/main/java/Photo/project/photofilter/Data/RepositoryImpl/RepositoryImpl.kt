package Photo.project.photofilter.Data.RepositoryImpl

import Photo.project.photofilter.Data.Room.PhotoDatabase
import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.Domain.Repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl : Repository {
    val dao = PhotoDatabase.createDatabase().getDAO()
    override suspend fun savePhoto(photoItem: PhotoItem) {
        dao.savePhoto(photoItem)
    }

    override suspend fun deletePhoto(photoItem: PhotoItem) {
        dao.deletePhoto(photoItem)
    }

    override suspend fun getPhotos(): Flow<List<PhotoItem>> {
        return dao.getPhotos()
    }
}