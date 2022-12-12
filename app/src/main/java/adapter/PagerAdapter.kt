package adapter

import fragments.fragmentUserProfile.FragmentUserProfile
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import fragments.fragmentContacts.FragmentContacts

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentUserProfile()
            else -> FragmentContacts()
        }
    }

}