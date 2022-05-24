package id.capstone.tanamin.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: ProfileUser,

	@field:SerializedName("status")
	val status: String
)

data class ProfileUser(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("address")
	val address: String?=null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("progress")
	val progress: Int?=null,

	@field:SerializedName("profile_picture")
	val profilePicture: String?=null,

	@field:SerializedName("finish")
	val finish: Int?=null,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int?=null
)
