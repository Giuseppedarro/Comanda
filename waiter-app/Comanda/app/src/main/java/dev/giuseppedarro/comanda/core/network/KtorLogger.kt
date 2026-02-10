package dev.giuseppedarro.comanda.core.network

import android.util.Log

/**
 * An abstraction for logging to allow for testable Ktor client setup.
 */
interface KtorLogger {
    fun log(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

/**
 * Production implementation of [KtorLogger] that uses Android's Logcat.
 */
class AndroidKtorLogger : KtorLogger {
    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}