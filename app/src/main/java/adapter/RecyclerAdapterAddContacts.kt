package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level5.R
import com.example.level5.databinding.RecyclerviewItemAddContactsBinding
import data.model.User
import extension.addImage
import fragments.fragmentAddContacts.FragmentAddContacts
import util.DiffUtil

class RecyclerAdapterAddContacts(private val addContactController: FragmentAddContacts) :
    ListAdapter<User, RecyclerAdapterAddContacts.ViewHolder>(DiffUtil) {

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_add_contacts, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            RecyclerviewItemAddContactsBinding.bind(itemView).run {
                val user = currentList[position]
                tvName.text = user.name
                tvCareer.text = user.career
                ivRecyclerItemUserPhoto.addImage(user.image)
                user.id = position

                containerItem.setOnClickListener {
                    println(user)
                    println(user.hashCode())
                    addContactController.onOpenContactProfile(user)
                }
                btnAddContact.setOnClickListener {
                    addContactController.onContactAdd(user)
                }
            }
        }
    }
}

