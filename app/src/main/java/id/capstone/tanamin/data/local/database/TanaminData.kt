package id.capstone.tanamin.data.local.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity (tableName = "classes")
@Parcelize
data class Classes(
    @PrimaryKey
    val id_class: Int,
    val title:String,
    val detail:String,
    val picture:String,
    val total_module:Int,
    val progress: Float,
    val modul_title: String,
    val modul_id: Int
) : Parcelable

@Entity (tableName = "modules")
data class Moduls(
    @PrimaryKey
    val id_moduls:Int,
    val classes_id:Int,
    val title:String,
    val content:String,
    val picture:String,
    val quiz_id:Int
)

@Entity (tableName = "informations")
data class Informations(
    @PrimaryKey
    val id_informations:Int,
    val name:String,
    val content:String,
    val benefit:String,
    val classes_id:Int
)