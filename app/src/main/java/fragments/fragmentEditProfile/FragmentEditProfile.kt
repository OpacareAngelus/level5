package fragments.fragmentEditProfile

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentEditProfileBinding
import kotlinx.coroutines.launch
import remote.requests.EditProfileRequest

class FragmentEditProfile : BaseFragment<FragmentEditProfileBinding>(
    FragmentEditProfileBinding::inflate
) {

    private lateinit var sharedPreferences: SharedPreferences
    private val editProfileViewModel: FragmentEditProfileViewModel by viewModels()

    private lateinit var selectedImageUri: Uri
    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data as Uri
                binding.ivUserPhoto.setImageURI(data?.data)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getPreferences(Application.MODE_PRIVATE)

        with(binding) {
            btnAddPhoto.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSaveContact.setOnClickListener {
                /*TODO*/
                val call = EditProfileRequest(
                    etUsername.text.toString(),
                    etPhone.text.toString(),
                    etAddress.text.toString(),
                    etCareer.text.toString(),
                    etDataOfBirth.text.toString(),
                )
                editProfileViewModel.editProfileRequest(
                    sharedPreferences.getString(getString(R.string.id), "").toString(),
                    getString(R.string.token_bearer) + sharedPreferences.getString(
                        getString(R.string.access_token),
                        ""
                    ), call
                )
                findNavController().popBackStack()
            }
        }
    }
}