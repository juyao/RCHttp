package com.juyao.httpdemo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.juyao.http.service.ServiceCreator
import com.juyao.http.viewmodel.ViewModelX


/**
 *
 *
 *Created by juyao on 11/23/20 at 1:25 AM.\n
 * 邮箱:juyao0909@gmail.com
 */


class MainViewModel: ViewModelX(){
    val service=ServiceCreator.create(GanHuoService::class.java)
    val bannerData:MutableLiveData<List<Banner>> = MutableLiveData<List<Banner>>()
    /**
     * 获取banner数据
     */
    fun getBanners(){
        apiRequest(
                {
                    service.getBanners()
                },
                bannerData,
            {
                //这里自行处理异常
                Log.i("MainViewModel","请求出现异常：${it.message}")
            }
        )
    }

//    /**
//     * 获取banner数据
//     */
//    fun getBanners(){
//        apiRequest(
//            {
//                service.getBanners()
//            },
//            {
//                //这里直接拿到想要请求的数据
//                Log.i("MainViewModel","请求到Banner数据list：$it")
//            }
//        )
//    }


}
