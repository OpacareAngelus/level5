package fragments.fragmentUserProfile

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentUserProfileBinding
import extension.addImage
import fragments.FragmentMainDirections

class FragmentUserProfile : BaseFragment<FragmentUserProfileBinding>(
    FragmentUserProfileBinding::inflate
) {
    private lateinit var accessToken: String
    private lateinit var sharedPreferences: SharedPreferences

    private val userProfileViewModel: FragmentUserProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accessToken =
            requireActivity().intent.getStringExtra(getString(R.string.access_token)).toString()

        sharedPreferences = requireActivity().getPreferences(Application.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sharedPreferences.edit()
        ed.putString(getString(R.string.access_token), accessToken).apply()
        ed.putString(
            getString(R.string.id),
            requireActivity().intent.getStringExtra(getString(R.string.current_user_id))
                .toString()
        ).apply()

        userProfileViewModel.requestUserData(
            requireActivity().intent.getStringExtra(getString(R.string.current_user_id))
                .toString(),
            getString(R.string.token_bearer) + accessToken
        )

        with(binding) {
            btnEditProfile.setOnClickListener {
                findNavController().navigate(
                    FragmentMainDirections.actionFragmentMainToFragmentEditProfile()
                )
            }
        }

        setObserver()
    }

    private fun setObserver() {
        userProfileViewModel.userData.observe(viewLifecycleOwner){
            with(binding){
            tvMyProfileName.text = it.name
            tvMyProfileCareer.text = it.career
            tvMyProfileAddress.text = it.address
            ivMyProfileUserPhoto.addImage(it.image)
            }
        }
    }
}