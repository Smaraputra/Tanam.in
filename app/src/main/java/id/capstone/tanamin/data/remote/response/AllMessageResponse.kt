package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllMessageResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("messege")
	val messege: String,

	@field:SerializedName("id_forum")
	val idForum: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("users_id")
	val usersId: Int,

	@field:SerializedName("id_reply_forum")
	val idReplyForum: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)
