package com.juyao.httpdemo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface DownloadService {
    @Streaming
    @GET
    suspend fun download(@Url url: String?): ResponseBody
}