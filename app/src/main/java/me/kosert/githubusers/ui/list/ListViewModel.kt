package me.kosert.githubusers.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import me.kosert.githubusers.data.paging.UsersDataSource
import me.kosert.githubusers.data.repository.IUserRepository

class ListViewModel(private val repo: IUserRepository) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = repo.batchSize, enablePlaceholders = true)

    val pagingFlow
        get() = Pager(config = pagingConfig, pagingSourceFactory = { UsersDataSource(repo) })
            .flow
            .cachedIn(viewModelScope)
}