package activity.mainActivity.fragments.fragmentContacts.fragmentAddContacts.fragmentUnattachedContactProfile

import activity.mainActivity.fragments.fragmentContacts.fragmentAddContacts.FragmentAddContactsArgs
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import base.BaseFragment
import com.example.level5.databinding.FragmentUnattachedContactProfileBinding
import extension.addImage

class FragmentUnattachedContactProfile : BaseFragment<FragmentUnattachedContactProfileBinding>(
    FragmentUnattachedContactProfileBinding::inflate) {
    private val args: FragmentAddContactsArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
        with(binding) {
            btnArrow.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun addData() {
        val user = args.user
        binding.apply {
            ivAddContactPhoto.addImage(user?.image)
            tvName.text = user?.name
            tvCareer.text = user?.career
            tvUserAddress.text = user?.address
        }
    }
}