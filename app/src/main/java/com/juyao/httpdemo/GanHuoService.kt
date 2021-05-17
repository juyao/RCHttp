package com.juyao.httpdemo

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 *
 *
 *Created by juyao on 11/21/20 at 1:41 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


 interface GanHuoService {
    @GET("api/v2/banners")
    suspend fun getBanners(): HttpResut<List<Banner>>

    @GET("api/v2/banners")
    suspend fun getBannersForAll(): AllBannerData

    @GET("api/v2/banners")
    fun getBannersForLiveData():LiveData<HttpResut<List<Banner>>>
}