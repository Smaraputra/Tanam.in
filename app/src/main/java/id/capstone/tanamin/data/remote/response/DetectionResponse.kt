package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetectionResponse(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("accuracy")
	val accuracy: Double,

	@field:SerializedName("id")
	val id: Int
)
