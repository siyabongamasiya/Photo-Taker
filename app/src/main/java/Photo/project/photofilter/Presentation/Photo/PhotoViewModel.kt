package Photo.project.photofilter.Presentation.Photo

import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.Domain.Models.UseCasePack
import Photo.project.photofilter.PhotoFilter
import Photo.project.photofilter.Presentation.Photos.MainActivity
import Photo.project.photofilter.Utils.getFileFromContentUri
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PhotoViewModel : ViewModel() {
    private val useCasePack = UseCasePack()
//    private var photolist = emptyList<PhotoItem>()
//    init {
//        viewModelScope.launch {
//            useCasePack.getPhotos.invoke().collect{newlistitems ->
//                photolist = newlistitems
//            }
//        }
//
//    }


    fun deletePhoto(name : String ,url : String,context: Photo){
        val photoItem = PhotoItem(url,name)
        viewModelScope.launch {
            useCasePack.deletePhotoItem.invoke(photoItem)
            withContext(Dispatchers.Main){
                Toast.makeText(PhotoFilter.context,"Success!!",Toast.LENGTH_SHORT).show()

                val intent = Intent(context,MainActivity :: class.java)
                context.startActivity(intent)
            }
        }
    }

    fun sharePhoto(uri: String,context : Context){
        val intent = Intent(Intent.ACTION_SEND).apply {
            setType("image/*")
            putExtra(Intent.EXTRA_STREAM,Uri.parse(uri))
            putExtra(Intent.EXTRA_COMPONENT_NAME,"Share using..")
        }

        context.startActivity(Intent.createChooser(intent,null))
    }
}