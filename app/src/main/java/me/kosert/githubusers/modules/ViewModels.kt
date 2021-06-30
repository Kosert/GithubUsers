package me.kosert.githubusers.modules

import me.kosert.githubusers.ui.list.ListViewModel
import me.kosert.githubusers.ui.user.UserProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { ListViewModel(get()) }

    viewModel { UserProfileViewModel(get(), get()) }

}