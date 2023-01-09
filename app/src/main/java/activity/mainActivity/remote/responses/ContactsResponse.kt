package activity.mainActivity.remote.responses

import data.model.User

data class ContactsResponse(
    val status: String,
    val code: Int,
    val message: String?,
    val data: ContactsList
)

data class ContactsList(
    val contacts: List<User>
)