package fragments.fragmentEditProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import remote.ServiceAPI
import remote.requests.EditProfileRequest
import remote.responses.ProfileResponse

class FragmentEditProfileViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _editProfileResponse = MutableLiveData<ProfileResponse>()
    val editProfileResponse: LiveData<ProfileResponse> = _editProfileResponse

    private val compositeDisposable = CompositeDisposable()

    fun editProfileRequest(userId: String, accessToken: String, call: EditProfileRequest) {
        compositeDisposable.add(
            (apl as ServiceAPI).requestAPI.editProfile(userId, accessToken, call)
                .subscribeOn(Schedulers.io()).subscribe({
                    _editProfileResponse.postValue(it)
                }, {

                })
        )
    }
}