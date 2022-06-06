package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetectionInformationResponse(

	@field:SerializedName("data")
	val data: DetectedData,

	@field:SerializedName("status")
	val status: String
)

data class DetectedData(

	@field:SerializedName("manfaat")
	val manfaat: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("kandungan")
	val kandungan: String
)
