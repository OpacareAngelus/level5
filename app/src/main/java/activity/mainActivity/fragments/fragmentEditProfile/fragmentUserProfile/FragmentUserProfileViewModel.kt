package activity.mainActivity.fragments.fragmentEditProfile.fragmentUserProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.model.Profile
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import activity.mainActivity.remote.ServiceAPI
import activity.mainActivity.remote.responses.ProfileResponse

class FragmentUserProfileViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse: LiveData<ProfileResponse> = _profileResponse

    private val _userData = MutableLiveData<Profile>()
    val userData: LiveData<Profile> = _userData

    private val compositeDisposable = CompositeDisposable()

    fun requestUserData(userId: String, accessToken: String) {
        viewModelScope.launch {
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.getProfile(
                    userId, accessToken
                ).subscribeOn(Schedulers.io()).subscribe({
                    _profileResponse.postValue(it)
                    _userData.postValue(it.data.user)
                }, {

                })
            )
        }
    }

    fun changeUserProfile(profile: Profile){
        _userData.postValue(profile)
    }
}