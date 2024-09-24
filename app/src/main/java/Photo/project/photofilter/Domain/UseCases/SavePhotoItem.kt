package Photo.project.photofilter.Domain.UseCases

import Photo.project.photofilter.Data.RepositoryImpl.RepositoryImpl
import Photo.project.photofilter.Domain.Models.PhotoItem

class SavePhotoItem {
    private val repositoryImpl = RepositoryImpl()

    suspend operator fun invoke(photoItem: PhotoItem){
        repositoryImpl.savePhoto(photoItem)
    }
}