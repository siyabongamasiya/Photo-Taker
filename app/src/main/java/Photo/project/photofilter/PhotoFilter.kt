package Photo.project.photofilter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class PhotoFilter : Application() {
    private lateinit var context : Context

    override fun onCreate() {
        super.onCreate()
        context = this
        PhotoFilter.context = context
    }

    @SuppressLint("StaticFieldLeak")
    companion object{
        lateinit var context : Context
    }
}