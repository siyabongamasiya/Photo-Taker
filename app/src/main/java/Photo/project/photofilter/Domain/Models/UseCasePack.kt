package Photo.project.photofilter.Domain.Models

import Photo.project.photofilter.Domain.UseCases.DeletePhotoItem
import Photo.project.photofilter.Domain.UseCases.GetPhotos
import Photo.project.photofilter.Domain.UseCases.SavePhotoItem

data class UseCasePack(
    val deletePhotoItem : DeletePhotoItem = DeletePhotoItem(),
    val getPhotos: GetPhotos = GetPhotos(),
    val savePhotoItem: SavePhotoItem = SavePhotoItem()
)
