package util

import data.model.User

interface AddContactController {
    fun onContactAdd(user: User)
    fun onOpenContactProfile(user: User)
}