package me.kosert.githubusers.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.kittinunf.result.Result
import me.kosert.githubusers.data.repository.IUserRepository
import me.kosert.githubusers.data.models.UserListModel

class UsersDataSource(private val repo: IUserRepository) : PagingSource<Int, UserListModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserListModel> {
        val sinceUserId = params.key
        return repo.getUsers(sinceUserId).toLoadResult()
    }

    override fun getRefreshKey(state: PagingState<Int, UserListModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun <E : Exception> Result<List<UserListModel>, E>.toLoadResult(): LoadResult<Int, UserListModel> {
        return fold({ list ->
            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = list.lastOrNull()?.id
            )
        }, { ex ->
            LoadResult.Error(ex)
        })
    }
}