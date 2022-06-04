package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuizResponse(

	@field:SerializedName("data")
	val data: QuizData,

	@field:SerializedName("status")
	val status: String
)

data class QuizData(

	@field:SerializedName("nextModule")
	val nextModule: Int,

	@field:SerializedName("quizid")
	val quizid: Int,

	@field:SerializedName("maxid")
	val maxid: Int,

	@field:SerializedName("module")
	val module: QuizModule,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)

data class Pilihan(
	@field:SerializedName("pilihan3")
	val pilihan3: String,

	@field:SerializedName("pilihan2")
	val pilihan2: String,

	@field:SerializedName("pilihan1")
	val pilihan1: String
)


data class QuizModule(
	@field:SerializedName("soal1")
	val soal1: SoalData,

	@field:SerializedName("soal2")
	val soal2: SoalData,

	@field:SerializedName("soal3")
	val soal3: SoalData,

	@field:SerializedName("soal4")
	val soal4: SoalData,

	@field:SerializedName("soal5")
	val soal5: SoalData
)

data class SoalData(
	@field:SerializedName("soal")
	val soal: String,

	@field:SerializedName("pilihan")
	val pilihan: Pilihan
)