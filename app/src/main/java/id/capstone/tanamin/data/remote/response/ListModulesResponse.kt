package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListModulesResponse(

	@field:SerializedName("data")
	val data: ListModuleData?,

	@field:SerializedName("status")
	val status: String
)

data class ListmodulItem(

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("id_moduls")
	val idModuls: Int
)

data class ListModuleData(

	@field:SerializedName("detail_kelas")
	val detailKelas: List<DetailKelasItem>,

	@field:SerializedName("listmodul")
	val listmodul: List<ListmodulItem>
)

data class DetailKelasItem(

	@field:SerializedName("id_class")
	val idClass: Int,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("total_module")
	val totalModule: Int
)
