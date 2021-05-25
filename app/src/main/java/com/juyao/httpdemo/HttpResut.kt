package com.juyao.httpdemo

import com.juyao.http.async.ResponseX


/**
 *
 *
 *Created by juyao on 11/23/20 at 1:11 AM.\n
 * 邮箱:juyao0909@gmail.com
 */


 class HttpResut<T>: ResponseX<T>() {
    var status:Int=0
    var data:T?=null

    override fun getRequestStatus(): Int {
        return if(status==100){
            ResponseX.SUCCESS
        }else{
            status
        }
    }

    override fun getRequestData(): T? {
        return data
    }

    override fun getErrorMsg(): String? {
        return ""
    }
}