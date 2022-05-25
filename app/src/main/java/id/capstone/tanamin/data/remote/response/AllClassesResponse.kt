package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName
import id.capstone.tanamin.data.local.database.Classes

data class AllClassesResponse(

	@field:SerializedName("data")
	val data: AllClassesData,

	@field:SerializedName("status")
	val status: String
)

data class AllClassesData(

	@field:SerializedName("class")
	val jsonMemberClass: List<Classes>
)
