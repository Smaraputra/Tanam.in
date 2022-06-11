package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName
import id.capstone.tanamin.data.local.database.Classes

data class HomeResponse(

	@field:SerializedName("data")
	val data: HomeData?,

	@field:SerializedName("status")
	val status: String
)

data class HomeData(

	@field:SerializedName("kelas")
	val kelas: List<Classes>
)
