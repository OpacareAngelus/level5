package activity.mainActivity.remote.responses

import data.model.User


data class AuthorizeResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val data: AuthorizeResponseBody
)

data class AuthorizeResponseBody(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)