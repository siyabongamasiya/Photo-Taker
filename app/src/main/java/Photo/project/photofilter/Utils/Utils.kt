package Photo.project.photofilter.Utils

import Photo.project.photofilter.PhotoFilter
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Throws

//@SuppressLint("SimpleDateFormat")
//@Throws(IOException :: class)
//fun createImageFile() : File {
//    //create file
//    val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//    val storageDir : File? = PhotoFilter.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//
//}


fun getFileFromContentUri(context: Context, contentUri: Uri): File? {
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null
    return try {
        // Get the file name
        val fileName = getFileName(contentResolver, contentUri)

        // Create a temporary file to write the content to
        val tempFile = File(context.cacheDir, fileName ?: "tempfile")
        inputStream = contentResolver.openInputStream(contentUri)
        outputStream = FileOutputStream(tempFile)

        // Copy data from input stream to output stream
        inputStream?.let { input ->
            BufferedInputStream(input).use { bufferedInputStream ->
                bufferedInputStream.copyTo(outputStream)
            }
        }

        // Return the temporary file
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}

private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var name: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            name = it.getString(index)
        }
    }
    return name
}

@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_",  /* prefix */
        ".jpg",               /* suffix */
        storageDir            /* directory */
    )
}

fun saveImageToExternalStorage(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        context.contentResolver.openOutputStream(it).use { outputStream ->
            if(outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream?.flush()
            }
        }
    }

    return uri
}

fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): Bitmap? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}