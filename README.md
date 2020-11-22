# RCHttp
一个基于Retrofit+kotlin协程的网络请求框架

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
	        implementation 'com.github.juyao:RCHttp:V1.0.0'
	}
```

# 开始使用

在Application的onCreate方法里进行初始化：

```kotlin
ServiceCreator.init("https://gank.io/",ArrayList(),ArrayList())
```

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
