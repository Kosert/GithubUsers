package me.kosert.githubusers.modules

import me.kosert.githubusers.data.repository.IUserRepository
import me.kosert.githubusers.data.repository.UserRepository
import org.koin.dsl.module

val userRepositoryModule = module {

    single<IUserRepository> { UserRepository() }

}