package com.example.todoss5.arch

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write


    fun getContent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}