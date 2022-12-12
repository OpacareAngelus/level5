package util

import data.model.User

interface ContactsListController {
    fun onDeleteUser(user: User)
    fun onContactAdd(user: User)
    fun onOpenContactProfile(user: User)
}