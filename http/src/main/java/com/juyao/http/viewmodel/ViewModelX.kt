package com.juyao.http.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.juyao.http.async.ResponseX
import com.juyao.http.callback.RCDownLoadListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.*


/**
 *
 *
 *Created by juyao on 11/22/20 at 9:20 PM.\n
 * 邮箱:juyao0909@gmail.com
 */


open class ViewModelX : ViewModel() {
    open var myOnFail: (Throwable) -> Unit = {
        Log.i("http_fail", "请求失败：${it.message}")
    }

    fun <T> apiRequest(
        request: suspend () -> ResponseX<T>?,
        onSuccess: (T?) -> Unit,
        onFail: (Throwable) -> Unit = myOnFail
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                val res: ResponseX<T>? = withContext(Dispatchers.IO) { request() }
                if (null != res) {
                    if (res.getRequestStatus() == ResponseX.SUCCESS) {
                        onSuccess(res.getRequestData())
                    } else {
                        onFail(Exception(res.getErrorMsg()))
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
        liveData: MutableLiveData<T>,
        onFail: (Throwable) -> Unit = myOnFail
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try {
                val res: ResponseX<T>? = withContext(Dispatchers.IO) { request() }
                if (null != res) {
                    if (res.getRequestStatus() == ResponseX.SUCCESS) {
                        liveData.value = res.getRequestData()
                    } else {
                        onFail(Exception(res.getErrorMsg()))
                    }
                } else {
                    onFail(Exception("返回值为空"))
                }
            } catch (e: Exception) {
                onFail(e)
            }
        }
    }



    //此方法只适用于下载文件并写入本地
    fun dowmLoadFile(
        desPath: String,
        fileName: String = "${System.currentTimeMillis()}",
        request: suspend () -> ResponseBody,
        onFail: (Throwable) -> Unit = myOnFail,
        listener: RCDownLoadListener
    ): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                val res: ResponseBody = request()
                if (res != null) {
                    //将Response写入到从磁盘中
                    //注意，这个方法是运行在子线程中的
                    writeResponseToDisk(desPath, fileName, res, listener)
                } else {
                    onFail(Exception("返回值为空"))
                    listener.onFail("返回值为空")
                }

            } catch (e: Exception) {
                onFail(e)
                listener.onFail(e.message)
            }
        }
    }

    private fun writeResponseToDisk(
        path: String,
        fileName: String,
        responseBody: ResponseBody,
        downloadListener: RCDownLoadListener
    ) {
        //从response获取输入流以及总大小
        writeFileFromIS(
            File("$path${File.separator}$fileName"),
            responseBody.byteStream(),
            responseBody.contentLength(),
            downloadListener
        )
    }

    private val sBufferSize = 8192

    //将输入流写入文件
    private fun writeFileFromIS(
        file: File,
        `is`: InputStream,
        totalLength: Long,
        downloadListener: RCDownLoadListener
    ) {
        //开始下载
        downloadListener.onStart()
        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists()) file.getParentFile().mkdir()
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                downloadListener.onFail("createNewFile IOException")
            }
        }
        Log.i("MainViewModel", "文件路径：${file.absoluteFile}");
        var os: OutputStream? = null
        var currentLength: Long = 0
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            val data = ByteArray(sBufferSize)
            var len: Int = 0
            while (`is`.read(data, 0, sBufferSize).also { len = it } != -1) {
                os.write(data, 0, len)
                currentLength += len.toLong()
                //计算当前下载进度
                downloadListener.onProgress((100 * currentLength / totalLength).toInt())
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath())
        } catch (e: IOException) {
            e.printStackTrace()
            downloadListener.onFail("IOException,${e.message}")
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                if (os != null) {
                    os.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}