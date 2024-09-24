package Photo.project.photofilter.Domain.UseCases

import Photo.project.photofilter.Data.RepositoryImpl.RepositoryImpl
import Photo.project.photofilter.Domain.Models.PhotoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GetPhotos {
    private val repositoryImpl = RepositoryImpl()


    suspend operator fun invoke() : Flow<List<PhotoItem>>{
        return repositoryImpl.getPhotos()
    }
}