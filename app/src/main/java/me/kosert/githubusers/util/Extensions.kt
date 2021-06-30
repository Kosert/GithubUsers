package me.kosert.githubusers.util

import android.view.LayoutInflater
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

// something like this is needed because
// .withLoadStateFooter() does not work for the initial loading
fun PagingDataAdapter<*, *>.withFooter(loadStateAdapter: LoadStateAdapter<*>): ConcatAdapter {
    addLoadStateListener { loadStates ->
        loadStateAdapter.loadState = if (loadStates.refresh is LoadState.NotLoading)
            loadStates.append
        else
            loadStates.refresh
    }
    return ConcatAdapter(this, loadStateAdapter)
}