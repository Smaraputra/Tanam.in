package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListForumResponse(

	@field:SerializedName("data")
	val data: ForumData?,

	@field:SerializedName("status")
	val status: String
)

data class ListForumItem(

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("title")
	val title: String
)

data class ForumData(

	@field:SerializedName("listforum")
	val listforum: List<ListForumItem>
)
