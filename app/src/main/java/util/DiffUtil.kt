package util

import androidx.recyclerview.widget.DiffUtil
import data.model.User

object DiffUtil : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.name == newItem.name

    }
}