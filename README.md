# RCHttp使用文档

# 集成方式

项目根目录添加jitpack maven仓库地址

```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# 添加依赖

```groovy
dependencies {
	        implementation 'com.github.juyao:RCHttp:v1.0.2'
	}
```

# 开始使用

在Application的onCreate方法里进行初始化：

```kotlin
ServiceCreator.init("baseurl/",ArrayList(),ArrayList())
```
不要忘了加"/"
后面两个拦截器参数如果不需要自定义，就传两个空数组（非null）

创建实体类，继承ResponseX



```kotlin
/**
 *
 *
 *Created by juyao on 11/23/20 at 1:11 AM.\n
 * 邮箱:juyao0909@gmail.com
 */

 class HttpResut<T>: ResponseX<T>() {
    var status:Int=0
    var data:T?=null

    override fun getRequestStatus(): Int {
        return if(status==100){
            ResponseX.SUCCESS
        }else{
            ResponseX.FAILE
        }
    }

    override fun getRequestData(): T? {
        return data
    }

    override fun getErrorMsg(): String? {
        return ""
    }
}
```

结合自身的业务场景，重写getRequestStatus()，getRequestData()方法，getRequestData()返回我们具体想要得到的对象数据，如果有错误信息，可以以getErrorMsg()返回

本框架基于ViewModel，新建ViewModel继承ViewModelX，通过ServiceCreator.create创建service，

```kotlin
interface GanHuoService {
    @GET("api/v2/banners")
    suspend fun getBanners(): HttpResut<List<Banner>>
}
```

网络请求目前提供了两种方式，一种是下面这种，通过传入livedata的方式

```kotlin
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
```

在Activity通过livedata的观察监听数据变化，因为协程部分用的是viewmodelscope，所以不用担心生命周期的问题

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_hello).setOnClickListener {
            viewModel.getBanners()
        }
        viewModel.bannerData.observe(this, {
            t -> Toast.makeText(this@MainActivity,"获取banner成功,条数:${t.size}",Toast.LENGTH_LONG).show()
        })

    }
}
```

另外一种方式是直接拿到想要的数据，至于怎么处理交由开发者自己

```kotlin
/**
     * 获取banner数据
     */
    fun getBanners(){
        apiRequest(
            {
                service.getBanners()
            },
            {
                //这里直接拿到想要请求的数据
                Log.i("MainViewModel","请求到Banner数据list：$it")
            }
        )
    }
```

以上两种请求方式没有对异常进行处理，默认是个空实现。如果开发者想要对请求异常进行处理，可以自行实现三个参数：

```kotlin
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
```

以上是RCHttp的基本用法，更多功能有待完善，敬请期待

# 2020/12/12  新增文件下载功能，并且实时返回进度

同样，先定义Service

```kotlin
interface DownloadService {
    @Streaming
    @GET
    suspend fun download(@Url url: String?): ResponseBody
}
```

> 跟正常接口写法基本一致，需要注意的是要添加@Streaming注解。
默认情况下，Retrofit在处理结果前会将服务器端的Response全部读进内存。如果服务器端返回的是一个非常大的文件，则容易发生oom。使用@Streaming的主要作用就是把实时下载的字节就立马写入磁盘，而不用把整个文件读入内存
。

同样ViewModel需要继承ViewModeX,在ViewModel发送下载请求

```kotlin
fun dowmLoadWZRY(url:String,path:String,fileName:String){
        dowmLoadFile(path,fileName,{downloadService.download(url)},{
            Log.i("MainViewModel","请求出现异常：${it.message}")
        },object :RCDownLoadListener{
            override fun onStart() {
                Log.i("MainViewModel","下载开始～～～")
            }

            override fun onProgress(progress: Int) {
                Log.i("MainViewModel","下载进度：${progress}")
            }

            override fun onFinish(path: String?) {
                Log.i("MainViewModel","下载完成，文件路径：${path}")
            }

            override fun onFail(errorInfo: String?) {
                Log.i("MainViewModel","下载出错：${errorInfo}")
            }
        })
    }
```

Activity里调用就可以了

```kotlin
viewModel.dowmLoadWZRY("https://imtt.dd.qq.com/16891/apk/B168BCBBFBE744DA4404C62FD18FFF6F.apk?fsname=com.tencent.tmgp.sgame_1.61.1.6_61010601.apk",
                                   externalCacheDir!!.absolutePath,"王者荣耀.apk")
```

怎么样，是不是so easy?

这里需要注意的一点就是，因为我这里普通请求跟下载文件用的是同一个retrofit对象，所以一开始设置的30s时间显然不够，这里有几种解决方案

1. 创建两个retrofit对象，分别设置对应的超时时间，普通请求跟下载请求分开用，但是这种方式很不优雅，很难受
2. 用户自定义拦截器，根据特定链接设置特定超时时间，这种方式对用户来说加大了网络库的使用复杂度，违背了我的初心，另外，项目大了后下载的地址越来越多，不好管理
3. 最后，痛定思痛，就将超时时间统一设置成了10分钟

```kotlin
private const val CALL_TIME_OUT = 10L
        private const val CONNECT_TIME_OUT = 10L
        private const val READ_TIME_OUT = 10L
        private const val WRITE_TIME_OUT = 10L
```

```kotlin
builder.callTimeout(CALL_TIME_OUT, TimeUnit.MINUTES)
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MINUTES)
            builder.readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
            builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.MINUTES)
```

下载文件到此也基本完成。