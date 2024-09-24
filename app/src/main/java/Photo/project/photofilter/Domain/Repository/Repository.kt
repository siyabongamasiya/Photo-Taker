package Photo.project.photofilter.Domain.Repository

import Photo.project.photofilter.Domain.Models.PhotoItem
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun savePhoto(photoItem: PhotoItem)

    suspend fun deletePhoto(photoItem: PhotoItem)

    suspend fun getPhotos() : Flow<List<PhotoItem>>
}