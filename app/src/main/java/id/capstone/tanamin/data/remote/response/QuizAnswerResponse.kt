package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuizAnswerResponse(

	@field:SerializedName("data")
	val data: ScoreData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class ScoreData(

	@field:SerializedName("score")
	val score: Int
)
