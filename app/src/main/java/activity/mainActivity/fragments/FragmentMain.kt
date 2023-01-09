package activity.mainActivity.fragments

import activity.mainActivity.adapter.PagerAdapter
import android.os.Bundle
import android.view.View
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMain : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
    }

    private fun initial() {
        binding.apply {
            vpMain.adapter = PagerAdapter(this@FragmentMain)
            tlMain.tabIconTint = null
            TabLayoutMediator(tlMain, vpMain){
                    tab, pos ->
                when(pos){
                    0 -> tab.text = getString(R.string.my_profile)
                    1 -> tab.text = getString(R.string.my_contacts)
                }
            }.attach()
        }
    }
}