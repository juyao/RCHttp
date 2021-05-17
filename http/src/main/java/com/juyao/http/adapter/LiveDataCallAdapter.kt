package com.juyao.http.adapter

import androidx.lifecycle.LiveData
import com.juyao.http.async.ResponseX
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<T>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<T>): LiveData<T> {
        return object:LiveData<T>(){
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)){
                    //确保执行一次
                    call.enqueue(object :Callback<T>{
                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(response.body())
                        }

                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val value=object :ResponseX<T>(){
                                override fun getRequestStatus(): Int {
                                   return FAILE
                                }

                                override fun getErrorMsg(): String? {
                                    return t.message?:"convert failure"
                                }

                                override fun getRequestData(): T? {
                                    return null
                                }

                            } as T
                            postValue(value)
                        }

                    })
                }
            }
        }
    }

}