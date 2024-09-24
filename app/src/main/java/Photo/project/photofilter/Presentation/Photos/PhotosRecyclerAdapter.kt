package Photo.project.photofilter.Presentation.Photos

import Photo.project.photofilter.Constants.PHOTONAME
import Photo.project.photofilter.Constants.URI
import Photo.project.photofilter.Domain.Models.PhotoItem
import Photo.project.photofilter.PhotoFilter
import Photo.project.photofilter.Presentation.Photo.Photo
import Photo.project.photofilter.R
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class PhotosRecyclerAdapter(val listofphotos : List<PhotoItem>) :
    RecyclerView.Adapter<PhotosRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photoitem,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listofphotos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.collectPhotoItem(listofphotos[position])
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){

        fun collectPhotoItem(photoItem: PhotoItem){
            val imageview : ImageView= itemView.findViewById(R.id.image)
            val textview : TextView = itemView.findViewById(R.id.photoname)

            textview.text = photoItem.name
            Glide.with(PhotoFilter.context)
                .load(Uri.parse(photoItem.url))
                .centerCrop()
                .into(imageview)

            itemView.setOnClickListener{
                val intent = Intent(MainActivity.maincontext,Photo :: class.java).apply {
                    putExtra(URI,photoItem.url)
                    putExtra(PHOTONAME,photoItem.name)
                }
                PhotoFilter.context.startActivity(intent)
            }
        }

    }
}