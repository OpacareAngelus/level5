package activity.authActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import remote.ServiceAPI
import remote.requests.LoginRequest
import remote.responses.AuthorizeResponse

class AuthActivityViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _authorizeResponse = MutableLiveData<AuthorizeResponse>()
    val authorizeResponse: LiveData<AuthorizeResponse> = _authorizeResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val compositeDisposable = CompositeDisposable()

    fun loginRequest(email: String, password: String) {
        val call = LoginRequest(
            email,
            password
        )
        compositeDisposable.add(
            (apl as ServiceAPI).requestAPI.login(call).subscribeOn(Schedulers.io())
                .subscribe({
                    CoroutineScope(Dispatchers.Main).launch {
                        _errorMessage.postValue(it.code.toString())
                        _authorizeResponse.postValue(it)
                    }
                }, {
                    _errorMessage.postValue(it.message)
                })
        )
    }
}