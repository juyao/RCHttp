package com.juyao.http.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juyao.http.async.ResponseX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *
 *
 *Created by juyao on 11/22/20 at 9:20 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


open class ViewModelX: ViewModel() {
    var myOnFail: (Throwable) -> Unit={

    }
     fun <T> apiRequest(
        request: suspend () -> ResponseX<T>?,
        onSuccess: (T?) -> Unit,
        onFail:(Throwable) -> Unit=myOnFail
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                val res: ResponseX<T>? = withContext(Dispatchers.IO) { request() }
                if (null != res) {
                    if(res.getRequestStatus()==0){
                        onSuccess(res.getRequestData())
                    }else{
                        onFail(Exception("状态值异常"))
                    }
                } else {
                    onFail(Exception("返回值为空"))
                }
            } catch (e: Exception) {
                onFail(e)
            }
        }
    }

    fun <T> apiRequest(
        request: suspend () -> ResponseX<T>?,
        liveData:MutableLiveData<T>,
        onFail:(Throwable) -> Unit=myOnFail
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                val res: ResponseX<T>? = withContext(Dispatchers.IO) { request() }
                if (null != res) {
                    if(res.getRequestStatus()==0){
                        liveData.value=res.getRequestData()
                    }else{
                        onFail(Exception("状态值异常"))
                    }
                } else {
                    onFail(Exception("返回值为空"))
                }
            } catch (e: Exception) {
                onFail(e)
            }
        }
    }
}