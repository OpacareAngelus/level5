package activity.mainActivity.remote.responses

import data.model.User

data class UsersResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val data: UsersList
)

data class UsersList(
    val users: List<User>
)