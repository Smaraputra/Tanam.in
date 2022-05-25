package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: ProfileData,

	@field:SerializedName("status")
	val status: String
)

data class ProfileData(

	@field:SerializedName("progress")
	val progress: Int?,

	@field:SerializedName("finish")
	val finish: Int?,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("address")
	val address: String?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String?,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int?
)
