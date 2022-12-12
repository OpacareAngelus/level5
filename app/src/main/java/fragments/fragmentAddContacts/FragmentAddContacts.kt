package fragments.fragmentAddContacts

import adapter.RecyclerAdapterAddContacts
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentAddContactsBinding
import data.model.User
import fragments.fragmentContacts.FragmentContactsViewModel
import util.AddContactController

class FragmentAddContacts :
    BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate),
    AddContactController {


    private lateinit var sharedPreferences: SharedPreferences

    private val addContactsViewModel: FragmentAddContactsViewModel by activityViewModels()
    private val contactsViewModel: FragmentContactsViewModel by activityViewModels()


    private val usersAdapter by lazy {
        RecyclerAdapterAddContacts(addContactController = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Application.MODE_PRIVATE)

        addContactsViewModel.requestUsersList(
            getString(R.string.token_bearer) + sharedPreferences.getString(
                getString(R.string.access_token),
                ""
            ).toString()
        )

        val recyclerView: RecyclerView = binding.rvUsers.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = null

        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)

        binding.btnArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        setObservers()
    }

    override fun onContactAdd(user: User) {
        contactsViewModel.requestAddContact(
            sharedPreferences.getString(getString(R.string.id), "").toString(),
            getString(R.string.token_bearer) + sharedPreferences.getString(
                getString(R.string.access_token),
                ""
            ).toString(),
            user
        )
    }

    override fun onOpenContactProfile(user: User) {
        findNavController().navigate(
            FragmentAddContactsDirections.actionFragmentAddContactsToFragmentUnattachedContactProfile(
                user
            )
        )
    }

    private fun setObservers() {
        addContactsViewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val user = addContactsViewModel.getUser(viewHolder.absoluteAdapterPosition) as User
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    onContactAdd(user)
                }
                ItemTouchHelper.RIGHT -> {
                    onOpenContactProfile(user)
                }
            }
        }
    }
}
