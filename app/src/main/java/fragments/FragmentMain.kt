package fragments

import adapter.PagerAdapter
import android.os.Bundle
import android.view.View
import base.BaseFragment
import com.example.level5.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMain : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
    }

    private fun initial() {
        with(binding) {
        vpMain.adapter = PagerAdapter(requireParentFragment())
            tlMain.tabIconTint = null
            TabLayoutMediator(tlMain, vpMain) { tab, pos ->
                when (pos) {
                    0 -> tab.text = "My Profile"
                    1 -> tab.text = "My Contacts"
                }
            }.attach()
        }
    }
}