package id.capstone.tanamin.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListForumResponse(

	@field:SerializedName("data")
	val data: ForumData?,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class ListForumItem(

	@field:SerializedName("id_forum")
	val id_forum: Int,

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("title")
	val title: String
) : Parcelable

data class ForumData(

	@field:SerializedName("listforum")
	val listforum: List<ListForumItem>
)
