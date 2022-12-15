package fragments.fragmentUserProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import remote.ServiceAPI
import remote.responses.ProfileResponse

class FragmentUserProfileViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse: LiveData<ProfileResponse> = _profileResponse

    private val compositeDisposable = CompositeDisposable()

    fun requestUserData(userId: String, accessToken: String) {
        viewModelScope.launch {
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.getProfile(
                    userId, accessToken
                ).subscribeOn(Schedulers.io()).subscribe({
                    _profileResponse.postValue(it)
                }, {

                })
            )
        }
    }
}