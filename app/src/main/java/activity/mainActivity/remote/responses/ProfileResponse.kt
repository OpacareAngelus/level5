package activity.mainActivity.remote.responses

import data.model.Profile

data class ProfileResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val data: ProfileResponseBody
)

data class ProfileResponseBody(
    val user : Profile
)