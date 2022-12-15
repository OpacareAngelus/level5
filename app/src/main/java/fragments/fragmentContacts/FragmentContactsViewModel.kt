package fragments.fragmentContacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.model.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import remote.ServiceAPI
import remote.requests.AddContactRequest

class FragmentContactsViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> = _userListLiveData

    private val compositeDisposable = CompositeDisposable()

    fun getUser(position: Int) = _userListLiveData.value?.get(position)

    private fun initList(users: List<User>) {
        _userListLiveData.postValue(users)
    }

    fun requestAddContact(userId: String, accessToken: String, user: User) {
        viewModelScope.launch {
            val call = AddContactRequest(
                user.id + 1
            )
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.addContact(
                    userId,
                    accessToken,
                    call
                ).subscribeOn(Schedulers.io()).subscribe({
                    _userListLiveData.postValue(it.data.contacts)
                }, {

                })
            )
        }
    }

    fun requestDeleteContact(userId: String, deletingContactId: String, accessToken: String) {
        viewModelScope.launch {
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.deleteContact(
                    userId,
                    deletingContactId,
                    accessToken
                ).subscribeOn(Schedulers.io()).subscribe({
                    initList(it.data.contacts)
                }, {

                })
            )
        }
    }
}