package Photo.project.photofilter.Presentation.Photos

import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.Domain.Models.UseCasePack
import Photo.project.photofilter.PhotoFilter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotosViewModel : ViewModel() {
    private val useCasePack = UseCasePack()

    private var _photos = MutableStateFlow<List<PhotoItem>>(emptyList())
    val photos = _photos.onStart {
        getPhotos()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getPhotos(){
        viewModelScope.launch {
            useCasePack.getPhotos.invoke().collect{photolist ->
                _photos.value = photolist
            }
        }
    }

    fun savePhoto(name : String ,url : String){
        val photoItem = PhotoItem(url,name.substringAfterLast("/"))
        viewModelScope.launch {
            useCasePack.savePhotoItem.invoke(photoItem)
        }
    }
}