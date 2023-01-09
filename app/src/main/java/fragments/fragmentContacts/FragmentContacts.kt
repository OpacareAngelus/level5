package fragments.fragmentContacts

import adapter.RecyclerAdapterUserContacts
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseFragment
import com.example.level5.R
import com.example.level5.databinding.FragmentUserContactsBinding
import com.google.android.material.snackbar.Snackbar
import data.model.User
import fragments.FragmentMainDirections
import util.ContactsListController
import util.RecyclerAdapterLookUp
import util.Selector

class FragmentContacts :
    BaseFragment<FragmentUserContactsBinding>(FragmentUserContactsBinding::inflate),
    ContactsListController, Selector {

    private val contactsViewModel: FragmentContactsViewModel by activityViewModels()

    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(contactsListController = this, selector = this)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Application.MODE_PRIVATE)

        binding.tvAddContact.setOnClickListener {
            findNavController().navigate(
                FragmentMainDirections.actionFragmentMainToFragmentAddContacts()
            )
        }

        binding.btnDeleteSelectedContacts.apply {
            setOnClickListener {
                contactsViewModel.deleteSelectedUsers(
                    sharedPreferences.getString(getString(R.string.id), "").toString(),
                    getString(R.string.token_bearer) + sharedPreferences.getString(
                        getString(R.string.access_token),
                        ""
                    ).toString()
                )
                usersAdapter.getTracker()?.clearSelection()
                visibility = View.INVISIBLE
            }
        }

        val recyclerView: RecyclerView = binding.rvContacts.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = null

        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)

        val tracker: SelectionTracker<Long> = SelectionTracker.Builder(
            getString(R.string.selection_id),
            recyclerView,
            StableIdKeyProvider(recyclerView),
            RecyclerAdapterLookUp(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        usersAdapter.setTracker(tracker)

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

    override fun onDeleteUser(user: User) {
        contactsViewModel.requestDeleteContact(
            sharedPreferences.getString(getString(R.string.id), "").toString(),
            (user.id).toString(),
            getString(R.string.token_bearer) + sharedPreferences.getString(
                getString(R.string.access_token),
                ""
            ).toString()
        )
    }

    override fun onOpenContactProfile(user: User) {
        findNavController().navigate(
            FragmentMainDirections.actionFragmentMainToFragmentContactProfile(user)
        )
    }

    override fun changeVisibility(selected: Boolean) {
        when (selected) {
            true -> binding.btnDeleteSelectedContacts.visibility = View.VISIBLE
            else -> binding.btnDeleteSelectedContacts.visibility = View.INVISIBLE
        }
    }

    private fun setObservers() {
        contactsViewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it.toMutableList())
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
            val user = contactsViewModel.getUser(viewHolder.absoluteAdapterPosition) as User
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    val messageText = String.format(getString(R.string.has_been_deleted), user.name)
                    val delMessage = Snackbar.make(
                        viewHolder.itemView,
                        messageText,
                        Snackbar.LENGTH_LONG
                    )
                    onDeleteUser(user)
                    delMessage.setAction(R.string.cancel) {
                        onContactAdd(user)
                    }
                    delMessage.show()
                }
                ItemTouchHelper.RIGHT -> {
                    onOpenContactProfile(user)
                }
            }
        }
    }
}
