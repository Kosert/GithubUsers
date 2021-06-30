package me.kosert.githubusers.util

import androidx.lifecycle.LifecycleOwner

interface ObservableEvent<T> {
    fun observe(owner: LifecycleOwner, callback: (t: T) -> Unit)
}

class LiveDataEvent<T> : ObservableEvent<T> {

    private val internalLiveData = KotlinLiveData<Event<T>?>(null)

    fun setValue(data : T) {
        internalLiveData.value = Event(data)
    }

    override fun observe(owner: LifecycleOwner, callback: (t: T) -> Unit) {
        internalLiveData.observe(owner) { event ->
            event?.getIfNotConsumed()?.let(callback)
        }
    }

    class Event<out T>(private val content: T) {

        var consumed = false
            private set

        fun getIfNotConsumed(): T? {
            return if (consumed) null
            else content.also { consumed = true }
        }

        fun peekContent(): T = content
    }
}

fun liveDataEvent() = LiveDataEvent<Unit>()

fun LiveDataEvent<Unit>.post() = setValue(Unit)

fun <T> LiveDataEvent<T>.post(value: T) = setValue(value)