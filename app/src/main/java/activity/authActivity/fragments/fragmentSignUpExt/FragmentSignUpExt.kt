package activity.authActivity.fragments.fragmentSignUpExt

import activity.mainActivity.MainActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentSignUpExtendedBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentSignUpExt :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toMainActivityIntent: Intent

    private val signUpExtViewModel: FragmentSignUpExtViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toMainActivityIntent = Intent(requireActivity(), MainActivity::class.java)

        val email: String = requireArguments().getString(getString(R.string.email)).toString()
        val password: String = requireArguments().getString(getString(R.string.password)).toString()
        val name = binding.etSignUpUserName.text.toString()
        val phone = binding.etSignUpUserPhone?.text.toString()

        sharedPreferences =
            requireActivity().getSharedPreferences(
                getString(R.string.api_preferences_name),
                AppCompatActivity.MODE_PRIVATE
            )

        with(binding) {
            btnSignUpForward.setOnClickListener {
                signUpExtViewModel.onRegisterRequest(email, password, name, phone)
                onCheckResponseCode(view)
            }
            btnSignUpCancel.setOnClickListener {
                backToActivity()
            }
        }
    }

    private fun onCheckResponseCode(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            while (toMainActivityIntent.getStringExtra(getString(R.string.current_user_id)) == null) {
                processDataForNextActivity()
                delay(25)
            }
            startActivity(toMainActivityIntent)
        }

        CoroutineScope(Dispatchers.Main).launch {
            while (signUpExtViewModel.code.value == null) {
                delay(25)
            }
            if (signUpExtViewModel.code.value.equals(getString(R.string.bad_request))) {
                Snackbar.make(
                    view,
                    getString(R.string.this_mail_using),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun processDataForNextActivity() {
        signUpExtViewModel.authorizeResponse.observe(viewLifecycleOwner) {
            toMainActivityIntent.putExtra(
                getString(R.string.access_token),
                it.data.accessToken
            )
        }
        signUpExtViewModel.authorizeResponse.observe(viewLifecycleOwner) {
            toMainActivityIntent.putExtra(
                getString(R.string.refresh_token),
                it.data.refreshToken
            )
        }
        signUpExtViewModel.authorizeResponse.observe(viewLifecycleOwner) {
            toMainActivityIntent.putExtra(
                getString(R.string.current_user_id),
                it.data.user.id.toString()
            )
        }
    }

    private fun backToActivity() {
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction().commit()
    }
}