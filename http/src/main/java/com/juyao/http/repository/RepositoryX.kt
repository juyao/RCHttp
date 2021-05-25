package com.juyao.http.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.juyao.http.async.ResponseX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class RepositoryX {
    //直接返回livedata
    fun <T> apiRequest( request: suspend () -> ResponseX<T>?): LiveData<T?> {
        return liveData(Dispatchers.Main){
            try {
                val res: ResponseX<T>? = withContext(Dispatchers.IO) { request() }
                if (null != res) {
                    if (res.getRequestStatus() == ResponseX.SUCCESS) {
                        emit(res.getRequestData())
                    } else {
                        onFail(Exception(res.getErrorMsg()))
                    }
                } else {
                    onFail(Exception("返回值为空"))
                }
            } catch (e: Exception) {
                onFail(e)
            }

        }
    }


    open fun onFail(e:Throwable,code:Int=ResponseX.FAILE){
        Log.i("http_fail", "请求失败：${e.message}")
    }
}