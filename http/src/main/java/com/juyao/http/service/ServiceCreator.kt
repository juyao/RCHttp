package com.juyao.http.service

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.concurrent.TimeUnit


/**
 *
 *
 *Created by juyao on 11/22/20 at 8:34 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


class ServiceCreator {
    companion object{
        private const val CALL_TIME_OUT = 30 * 1000L
        private const val CONNECT_TIME_OUT = 30 * 1000L
        private const val READ_TIME_OUT = 30 * 1000L
        private const val WRITE_TIME_OUT = 30 * 1000L
        private var baseUrl: String = ""
        private val interceptors: ArrayList<Interceptor> = ArrayList()
        private val networkInterceptors: ArrayList<Interceptor> = ArrayList()
        @JvmStatic
        fun init(baseUrl:String,interceptors: List<Interceptor>, networkInterceptors: List<Interceptor>){
            Companion.baseUrl = baseUrl
            Companion.interceptors.clear()
            Companion.interceptors.addAll(interceptors)
            Companion.networkInterceptors.clear()
            Companion.networkInterceptors.addAll(networkInterceptors)
        }

        private val retrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            val builder = OkHttpClient.Builder()
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
            for (networkInterceptor in networkInterceptors) {
                builder.addNetworkInterceptor(networkInterceptor)
            }
            builder.callTimeout(CALL_TIME_OUT, TimeUnit.MILLISECONDS)
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
            builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }
        @JvmStatic
        fun <T> create(clazz: Class<T>): T {
            return retrofit.create(clazz)
        }
    }
}