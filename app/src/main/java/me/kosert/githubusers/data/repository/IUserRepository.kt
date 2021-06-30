package me.kosert.githubusers.data.repository

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import me.kosert.githubusers.data.models.FullUserModel
import me.kosert.githubusers.data.models.UserListModel

interface IUserRepository {

    val batchSize: Int
    suspend fun getUsers(sinceUserId: Int?): Result<List<UserListModel>, FuelError>
    suspend fun getFullUser(username: String): Result<FullUserModel, FuelError>
}