package me.kosert.githubusers.util

import androidx.lifecycle.*

@Suppress("UNCHECKED_CAST")
class KotlinLiveData<T>(default: T) : MutableLiveData<T>() {

    init {
        this.value = default
    }

    override fun getValue() = super.getValue() as T

    fun observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(owner, Observer {
            observer(it)
        })
    }
}