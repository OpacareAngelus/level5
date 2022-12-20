package fragments.fragmentSignUpExt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import remote.ServiceAPI
import remote.requests.RegisterUserRequest
import remote.responses.AuthorizeResponse

class FragmentSignUpExtViewModel(private val apl: Application) : AndroidViewModel(apl) {

    private val _authorizeResponse = MutableLiveData<AuthorizeResponse>()
    val authorizeResponse: LiveData<AuthorizeResponse> = _authorizeResponse

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private val compositeDisposable = CompositeDisposable()

    fun onRegisterRequest(
        email: String,
        password: String,
        name: String,
        phone: String
    ) {
        val call = RegisterUserRequest(email, password, name, phone)
        viewModelScope.launch {
                compositeDisposable.add(
                    (apl as ServiceAPI).requestAPI.registerUser(call)
                        .subscribeOn(Schedulers.io()).subscribe({
                            _code.postValue(it.code.toString())
                            _authorizeResponse.postValue(it)
                        }, {
                            _code.postValue(it.message)
                        })
                )
        }
    }

}