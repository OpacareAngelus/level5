package activity.authActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import activity.mainActivity.remote.ServiceAPI
import activity.mainActivity.remote.requests.LoginRequest
import activity.mainActivity.remote.responses.AuthorizeResponse

class AuthActivityViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _authorizeResponse = MutableLiveData<AuthorizeResponse>()
    val authorizeResponse: LiveData<AuthorizeResponse> = _authorizeResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val compositeDisposable = CompositeDisposable()

    fun loginRequest(email: String, password: String) {
        viewModelScope.launch {
            val call = LoginRequest(
                email,
                password
            )
            compositeDisposable.add(
                (apl as ServiceAPI).requestAPI.login(call).subscribeOn(Schedulers.io())
                    .subscribe({
                        _errorMessage.postValue(it.code.toString())
                        _authorizeResponse.postValue(it)
                    }, {
                        _errorMessage.postValue(it.message)
                    })
            )
        }
    }
}