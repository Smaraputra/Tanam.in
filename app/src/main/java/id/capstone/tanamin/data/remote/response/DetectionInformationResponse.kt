package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetectionInformationResponse(

	@field:SerializedName("data")
	val data: DetectionData,

	@field:SerializedName("status")
	val status: String
)

data class DetectionData(

	@field:SerializedName("manfaat")
	val manfaat: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("kandungan")
	val kandungan: String
)
