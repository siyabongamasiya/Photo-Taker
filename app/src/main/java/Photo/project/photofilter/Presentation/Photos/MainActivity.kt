package Photo.project.photofilter.Presentation.Photos


import Photo.project.photofilter.Constants.PHOTONAME
import Photo.project.photofilter.Constants.URI
import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.Presentation.Photo.Photo
import Photo.project.photofilter.Utils.createImageFile
import Photo.project.photofilter.Utils.getBitmapFromUri
import Photo.project.photofilter.Utils.getFileFromContentUri
import Photo.project.photofilter.Utils.saveImageToExternalStorage
import Photo.project.photofilter.databinding.PhotosBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {

    private var photolist = emptyList<PhotoItem>()
    private var adapter : PhotosRecyclerAdapter? = null
    private val photosViewModel = PhotosViewModel()
    private lateinit var binding : PhotosBinding
    private var currentUri : Uri? = null
    private val takepicture: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = getBitmapFromUri(currentUri!!,contentResolver)
                saveImageToExternalStorage(this,bitmap!!,"ciciro")

                val intent = Intent(this, Photo::class.java).apply {
                    putExtra(URI, currentUri.toString())
                    putExtra(PHOTONAME, currentUri?.lastPathSegment)
                }

                photosViewModel.savePhoto(currentUri!!.lastPathSegment!!, currentUri!!.toString())

                startActivity(intent)
            }
        }

    private var requestPermissionsLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                // Permissions granted
                onPermissionsGranted()
            }
            else -> {
                // Permissions denied
                onPermissionsDenied()
            }
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()

        binding = PhotosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        setRecycler(photolist)
        collectPhotos()
    }

    private fun collectPhotos(){
        val coroutine = CoroutineScope(Dispatchers.Default)

        coroutine.launch {
            photosViewModel.photos.collect{newphotoslist ->
                withContext(Dispatchers.Main){
                    setRecycler(newphotoslist)
                }

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecycler(listofphotos : List<PhotoItem>){
        if (listofphotos.size > 0) {
            makelistvisible()
            binding.photoslist.layoutManager = LinearLayoutManager(this)
            adapter = PhotosRecyclerAdapter(listofphotos)
            binding.photoslist.adapter = adapter
        }else{
            makelistinvisible()
        }
    }

    private fun setListeners(){
        binding.floatingActionButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = createImageFile(this)
                    photoFile?.also {
                        currentUri = FileProvider.getUriForFile(
                            this,
                            "${packageName}.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri)
                        takepicture.launch(takePictureIntent)
                    }
                }
            }
        }
    }

    private fun makelistvisible(){
        binding.emptytext.isVisible = false
        binding.photoslist.isVisible = true
    }

    private fun makelistinvisible(){
        binding.emptytext.isVisible = true
        binding.photoslist.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        photosViewModel.getPhotos()
    }
    private fun checkPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsNeeded.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            // Permissions already granted
            onPermissionsGranted()
        }
    }

    private fun onPermissionsGranted() {
        maincontext = this
    }

    private fun onPermissionsDenied() {
        System.exit(0)
    }

    companion object{
        lateinit var maincontext : Context
    }
}