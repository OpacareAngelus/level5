package activity.authActivity

import activity.MainActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.level5.R
import com.example.level5.databinding.ActivityAuthBinding
import fragments.fragmentSignUpExt.FragmentSignUpExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var toMainActivityIntent: Intent

    private lateinit var sharedPreferences: SharedPreferences

    private val authActivityViewModel: AuthActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toMainActivityIntent = Intent(this, MainActivity::class.java)
        sharedPreferences =
            getSharedPreferences(getString(R.string.api_preferences_name), MODE_PRIVATE)
        setListeners()
        loadFieldText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.email), binding.etEmail.text.toString())
        outState.putString(getString(R.string.password), binding.etPassword.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(binding) {
            etEmail.setText(savedInstanceState.getString(getString(R.string.email), ""))
            etPassword.setText(savedInstanceState.getString(getString(R.string.password), ""))
        }
    }

    override fun onStop() {
        super.onStop()
        if (binding.cbRemember.isChecked) saveFieldText()
    }

    private fun setListeners() {
        with(binding) {
            btnRegistration.setOnClickListener {
                regUser()
            }
            tvSignIn.setOnClickListener {
                authActivityViewModel.loginRequest(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
                onCheckResponseCode()
            }
        }
    }

    private fun onCheckResponseCode() {
        CoroutineScope(Dispatchers.Main).launch {
            while (toMainActivityIntent.getStringExtra(getString(R.string.current_user_id)) == null
            ) {
                processDataForNextActivity()
                delay(25)
                if (authActivityViewModel.errorMessage.value.equals(getString(R.string.user_unauthorized))) {
                    break
                }
            }
            if (authActivityViewModel.errorMessage.value.equals(getString(R.string.request_success))) {
                startActivity(toMainActivityIntent)
            } else {
                binding.etPassword.error =
                    getString(R.string.invalid_password)
                sharedPreferences.edit().remove(getString(R.string.currentRequestStatus))
                    .apply()
            }
        }
    }

    private fun processDataForNextActivity() {
        val editSharedPreferences: SharedPreferences.Editor = sharedPreferences.edit()
        authActivityViewModel.authorizeResponse.observe(this) {
            editSharedPreferences.putString(
                getString(R.string.currentRequestStatus),
                it.code.toString()
            ).apply()
        }
        authActivityViewModel.authorizeResponse.observe(this) {
            toMainActivityIntent.putExtra(
                getString(R.string.access_token),
                it.data.accessToken
            )
        }
        authActivityViewModel.authorizeResponse.observe(this) {
            toMainActivityIntent.putExtra(
                getString(R.string.refresh_token),
                it.data.refreshToken
            )
        }
        authActivityViewModel.authorizeResponse.observe(this) {
            toMainActivityIntent.putExtra(
                getString(R.string.current_user_id),
                it.data.user.id.toString()
            )
        }
    }

    private fun regUser() {
        val signUpExtended = FragmentSignUpExt()
        val args = Bundle()
        args.putString(getString(R.string.email), binding.etEmail.text.toString())
        args.putString(getString(R.string.password), binding.etPassword.text.toString())
        signUpExtended.arguments = args
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.signUpExtended, signUpExtended)
            .commit()
    }

    private fun saveFieldText() {
        sharedPreferences = getPreferences(MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sharedPreferences.edit()
        ed.putString(getString(R.string.email), binding.etEmail.text.toString())
        ed.putString(getString(R.string.password), binding.etPassword.text.toString())
        ed.apply()
    }

    private fun loadFieldText() {
        sharedPreferences = getPreferences(MODE_PRIVATE)
        with(binding) {
            etEmail.setText(sharedPreferences.getString(getString(R.string.email), ""))
            etPassword.setText(sharedPreferences.getString(getString(R.string.password), ""))
        }
    }

}

