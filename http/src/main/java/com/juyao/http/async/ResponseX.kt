package com.juyao.http.async


/**
 *
 *
 *Created by juyao on 11/22/20 at 9:37 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


abstract class ResponseX<T>{
    abstract fun getRequestStatus():Int
    abstract fun getErrorMsg():String?
    abstract fun getRequestData():T?
    companion object{
        const val SUCCESS=0
        const val FAILE=-1226
    }

}