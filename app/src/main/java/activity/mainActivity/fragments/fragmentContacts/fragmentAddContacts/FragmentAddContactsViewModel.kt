package activity.mainActivity.fragments.fragmentContacts.fragmentAddContacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.model.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import activity.mainActivity.remote.ServiceAPI

class FragmentAddContactsViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> = _userListLiveData

    fun getUser(position: Int) = _userListLiveData.value?.get(position)

    fun initList(users: List<User>) {
        _userListLiveData.postValue(users)
    }

    private val compositeDisposable = CompositeDisposable()

    fun requestUsersList(accessToken: String) {
        viewModelScope.launch {
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.getUsers(
                    accessToken
                ).subscribeOn(Schedulers.io()).subscribe({
                    initList(it.data.users)
                }, {

                })
            )
        }
    }
}