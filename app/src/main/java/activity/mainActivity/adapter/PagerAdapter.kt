package activity.mainActivity.adapter

import activity.mainActivity.fragments.fragmentEditProfile.fragmentUserProfile.FragmentUserProfile
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import activity.mainActivity.fragments.fragmentContacts.FragmentContacts

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