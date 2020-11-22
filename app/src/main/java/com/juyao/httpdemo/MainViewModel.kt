package com.juyao.httpdemo

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
                bannerData
        )
    }

}
