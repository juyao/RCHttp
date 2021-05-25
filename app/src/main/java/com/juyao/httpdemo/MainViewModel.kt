package com.juyao.httpdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.juyao.http.callback.RCDownLoadListener
import com.juyao.http.service.ServiceCreator
import com.juyao.http.viewmodel.ViewModelX


/**
 *
 *
 *Created by juyao on 11/23/20 at 1:25 AM.\n
 * 邮箱:juyao0909@gmail.com
 */


class MainViewModel : ViewModelX() {

    val service = ServiceCreator.create(GanHuoService::class.java)
    val downloadService = ServiceCreator.createDownLoadService(DownloadService::class.java)
    val bannerData: MutableLiveData<List<Banner>> = MutableLiveData<List<Banner>>()

    /**
     * 获取banner数据
     */
    fun getBanners() {
        apiRequest(
            {
                service.getBanners()
            },
            bannerData
        )
    }



    fun getBannerLiveData(): LiveData<List<Banner>?> {
        return apiRequest({service.getBanners()})
    }


    fun getBannersForAll() {
        apiRequest(
            request = { service.getBannersForAll() },
            onSuccess = {
                Log.i(
                    "MainViewModel",
                    "请求到Banner数据list：status=${it!!.status},size=${it.data!!.size}"
                )
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

    override fun onFail(e: Throwable, code: Int) {
        super.onFail(e, code)

    }

    fun dowmLoadWZRY(url: String, path: String, fileName: String) {
        dowmLoadFile(path, fileName, { downloadService.download(url) }, object : RCDownLoadListener {
            override fun onStart() {
                Log.i("MainViewModel", "下载开始～～～")
            }

            override fun onProgress(progress: Int) {
                Log.i("MainViewModel", "下载进度：${progress}")
            }

            override fun onFinish(path: String?) {
                Log.i("MainViewModel", "下载完成，文件路径：${path}")
            }

            override fun onFail(errorInfo: String?) {
                Log.i("MainViewModel", "下载出错：${errorInfo}")
            }
        })
    }


}
