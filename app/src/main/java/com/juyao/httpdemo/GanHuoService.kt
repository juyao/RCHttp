package com.juyao.httpdemo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 *
 *
 *Created by juyao on 11/21/20 at 1:41 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


 interface GanHuoService {
    @GET("api/v2/banners")
    suspend fun getBanners(): HttpResut<List<Banner>>
}