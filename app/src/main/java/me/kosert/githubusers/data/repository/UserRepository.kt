package me.kosert.githubusers.data.repository

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.kosert.githubusers.App
import me.kosert.githubusers.data.models.FullUserModel
import me.kosert.githubusers.data.models.UserListModel
import me.kosert.githubusers.util.Log

private const val BASE_PATH = "https://api.github.com"
private const val GITHUB_ACCEPT_HEADER = "application/vnd.github.v3+json"
private const val PARAM_SINCE = "since"
private const val PARAM_PER_PAGE = "per_page"

class UserRepository : IUserRepository {

    init {
        FuelManager.instance.apply {
            basePath = BASE_PATH
            baseHeaders = mapOf(
                Headers.ACCEPT to GITHUB_ACCEPT_HEADER
            )
        }
    }

    private val gson = GsonBuilder().apply {
        if (App.isDebug) setPrettyPrinting()
    }.create()

    override val batchSize: Int
        get() = 15

    override suspend fun getUsers(sinceUserId: Int?): Result<List<UserListModel>, FuelError> {

        val listType = TypeToken.getParameterized(List::class.java, UserListModel::class.java).type

        return Fuel.get("/users")
            .setNonNullParameters(PARAM_SINCE to sinceUserId, PARAM_PER_PAGE to batchSize)
            .awaitStringResult()
            .map { gson.fromJson<List<UserListModel>>(it, listType) }
            .also { Log.d("Request users: $it") }
    }

    override suspend fun getFullUser(username: String): Result<FullUserModel, FuelError> {
        return Fuel.get("/users/$username")
            .awaitStringResult()
            .map { gson.fromJson(it, FullUserModel::class.java) }
            .also { Log.d("Request user '$username': $it") }
    }

    private fun Request.setNonNullParameters(vararg params: Pair<String, Any?>?) = apply {
        this.parameters = params.filterNotNull().filter { it.second != null }
    }
}