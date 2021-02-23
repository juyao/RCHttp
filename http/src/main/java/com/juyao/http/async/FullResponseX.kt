package com.juyao.http.async

open class FullResponseX<T>: ResponseX<T>() {
    override fun getRequestStatus(): Int {
        return SUCCESS
    }

    override fun getErrorMsg(): String? {
        return ""
    }

    override fun getRequestData(): T? {
        return this as T
    }
}