package me.kosert.githubusers.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import me.kosert.githubusers.databinding.ItemUserErrorBinding
import me.kosert.githubusers.util.Log

class UserLoadStateAdapter(
    private val onClick: () -> Unit
) : LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(
        val viewBinding: ItemUserErrorBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val isError = loadState is LoadState.Error
        holder.viewBinding.root.apply {
            isVisible = isError
            setOnClickListener { onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LoadStateViewHolder(ItemUserErrorBinding.inflate(inflater, parent, false))
    }
}