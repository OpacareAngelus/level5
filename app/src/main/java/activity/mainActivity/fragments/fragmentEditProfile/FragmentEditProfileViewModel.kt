package activity.mainActivity.fragments.fragmentEditProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import activity.mainActivity.remote.ServiceAPI
import activity.mainActivity.remote.requests.EditProfileRequest
import activity.mainActivity.remote.responses.ProfileResponse

class FragmentEditProfileViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _editProfileResponse = MutableLiveData<ProfileResponse>()
    val editProfileResponse: LiveData<ProfileResponse> = _editProfileResponse

    private val compositeDisposable = CompositeDisposable()

    fun editProfileRequest(userId: String, accessToken: String, call: EditProfileRequest) {
        viewModelScope.launch {
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.editProfile(userId, accessToken, call)
                    .subscribeOn(Schedulers.io()).subscribe({
                        _editProfileResponse.postValue(it)
                    }, {

                    })
            )
        }
    }
}