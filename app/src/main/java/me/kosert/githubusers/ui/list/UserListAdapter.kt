package me.kosert.githubusers.ui.list

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.kosert.githubusers.R

import me.kosert.githubusers.data.models.UserListModel
import me.kosert.githubusers.databinding.ItemUserBinding
import me.kosert.githubusers.util.getRandomColor

class UserListAdapter(
    val onUserClicked: (userName: String) -> Unit
) : PagingDataAdapter<UserListModel, UserListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val viewBinding: ItemUserBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.viewBinding.apply {

            user?.let {
                shimmerLayout.isVisible = false
                contentLayout.isVisible = true
                nickText.text = it.login
                Glide.with(this.root)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(avatarImageView)
                avatarImageView.backgroundTintList = ColorStateList.valueOf(getRandomColor())

                root.setOnClickListener { onUserClicked(user.login) }
            } ?: run {
                shimmerLayout.isVisible = true
                contentLayout.isVisible = false
                root.setOnClickListener(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    object DiffCallback : DiffUtil.ItemCallback<UserListModel>() {

        override fun areItemsTheSame(oldItem: UserListModel, newItem: UserListModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserListModel, newItem: UserListModel): Boolean {
            return true // no dynamic content
        }
    }
}