package Photo.project.photofilter.Domain.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PhotoItem(
    @PrimaryKey
    val url : String,
    val name : String
)
