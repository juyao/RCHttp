package com.juyao.httpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_hello).setOnClickListener {
           viewModel.getBannersForAll()
//            viewModel.dowmLoadWZRY("https://imtt.dd.qq.com/16891/apk/B168BCBBFBE744DA4404C62FD18FFF6F.apk?fsname=com.tencent.tmgp.sgame_1.61.1.6_61010601.apk&csr=1bbd",
//                                   externalCacheDir!!.absolutePath,"王者荣耀.apk")

        }
        viewModel.bannerData.observe(this, Observer<List<Banner>> { t -> Toast.makeText(this@MainActivity,"获取banner成功,条数:${t?.size}",Toast.LENGTH_LONG).show() }
        )


    }
}