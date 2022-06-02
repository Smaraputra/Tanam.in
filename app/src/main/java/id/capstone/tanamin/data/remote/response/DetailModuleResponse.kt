package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailModuleResponse(

	@field:SerializedName("data")
	val data: DataDetailModule,

	@field:SerializedName("status")
	val status: String
)

data class ModuleItem(

	@field:SerializedName("quiz_id")
	val quizId: Any,

	@field:SerializedName("classes_id")
	val classesId: Int,

	@field:SerializedName("id_moduls")
	val idModuls: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("picture")
	val picture: Any,

	@field:SerializedName("total_module")
	val totalModule: Int
)

data class DataDetailModule(

	@field:SerializedName("nextModule")
	val nextModule: Int,

	@field:SerializedName("module")
	val module: List<ModuleItem>,

	@field:SerializedName("class_id")
	val classId: String,

	@field:SerializedName("maxid")
	val maxId:Int
)
