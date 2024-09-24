package Photo.project.photofilter.Presentation.Photo

import Photo.project.photofilter.Constants.PHOTONAME
import Photo.project.photofilter.Constants.URI
import Photo.project.photofilter.Presentation.Photos.MainActivity
import Photo.project.photofilter.databinding.PhotoBinding
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class Photo : AppCompatActivity() {
    private lateinit var photoBinding: PhotoBinding
    val photoViewModel = PhotoViewModel()
    private var uri : String? = null
    private var name : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoBinding = PhotoBinding.inflate(LayoutInflater.from(this))
        setContentView(photoBinding.root)
        setListerners()

        uri = intent.getStringExtra(URI)
        name = intent.getStringExtra(PHOTONAME)

        Glide.with(this)
            .load(Uri.parse(uri))
            .centerCrop()
            .into(photoBinding.image)
    }

    private fun setListerners(){
        photoBinding.share.setOnClickListener {
            sharePhoto()
        }

        photoBinding.delete.setOnClickListener {
            delete()
        }

        photoBinding.toolbar.setNavigationOnClickListener {
            val intent = Intent(this,MainActivity :: class.java)
            startActivity(intent)
        }
    }

    private fun sharePhoto(){
        photoViewModel.sharePhoto(uri!!,this)
    }

    private fun delete(){
        photoViewModel.deletePhoto(name!!.substringAfterLast("/"),uri!!,this)
    }
}