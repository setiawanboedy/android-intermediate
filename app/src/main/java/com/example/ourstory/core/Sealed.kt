package com.example.ourstory.core

data class Sealed<out T>(val status: Status, val data: T?, val message: String?){
    companion object{
        fun <T> success(data: T?): Sealed<T> = Sealed(Status.SUCCESS, data, null)
        fun <T> error(message: String?, data: T) = Sealed(Status.ERROR, data, message)
        fun <T> loading(data: T?) = Sealed(Status.LOADING, data, null)
    }
}