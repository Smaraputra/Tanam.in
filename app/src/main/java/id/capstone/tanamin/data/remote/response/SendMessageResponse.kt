package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
