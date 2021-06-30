package me.kosert.githubusers.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.kosert.githubusers.data.models.FullUserModel
import me.kosert.githubusers.data.repository.IUserRepository
import me.kosert.githubusers.util.KotlinLiveData
import me.kosert.githubusers.util.ObservableEvent
import me.kosert.githubusers.util.liveDataEvent
import me.kosert.githubusers.util.post

class UserProfileViewModel(
    private val userName: String,
    private val repo: IUserRepository
) : ViewModel() {

    private val _userData = KotlinLiveData<FullUserModel?>(null)
    val userData: LiveData<FullUserModel?>
        get() = _userData

    private val _isProgress = KotlinLiveData(false)
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    private val _errorEvent = liveDataEvent()
    val errorEvent: ObservableEvent<Unit>
        get() = _errorEvent

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            requestUser()
        }
    }

    private suspend fun requestUser() = withContext(Dispatchers.Main) {
        _isProgress.value = true

        repo.getFullUser(userName).fold({
            _userData.value = it
        }, {
            _errorEvent.post()
        })

        _isProgress.value = false
    }
}