package me.kosert.githubusers.util

import android.util.Log
import me.kosert.githubusers.App


private const val appTag = "GitHubUsers"

open class Logger(
    logTag: String? = null
) {

    protected val tag: String = logTag?.let { "$appTag.$it" } ?: appTag

    fun d(message: String?) {
        if (!App.isDebug) return
        Log.d(tag, message.toString())
    }

    fun i(message: String?) {
        if (!App.isDebug) return
        Log.i(tag, message.toString())
    }

    fun w(message: String?) {
        if (!App.isDebug) return
        Log.w(tag, message.toString())
    }

    fun e(message: String?) {
        if (!App.isDebug) return
        Log.e(tag, message.toString())
    }

    fun e(exception: Throwable) {
        if (!App.isDebug) return
        Log.e(tag, "Non-fatal exception:", exception)
    }

    fun e(message: String?, exception: Throwable) {
        if (!App.isDebug) return
        Log.e(tag, message.toString(), exception)
    }

    fun list(list: List<Any>) {
        if (!App.isDebug) return
        list.forEachIndexed { index, any ->
            Log.d(tag, "$index: $any")
        }
    }
}

object Log : Logger()