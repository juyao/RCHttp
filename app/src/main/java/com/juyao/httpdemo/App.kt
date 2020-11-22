package com.juyao.httpdemo

import android.app.Application
import com.juyao.http.service.ServiceCreator
import okhttp3.Interceptor


/**
 *
 *
 *Created by juyao on 11/23/20 at 1:03 AM.\n
 * 邮箱:juyao0909@gmail.com
 */


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceCreator.init("https://gank.io/",ArrayList(),ArrayList())
    }
}