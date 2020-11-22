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
            viewModel.getBanners()
        }
        viewModel.bannerData.observe(this, {
            t -> Toast.makeText(this@MainActivity,"获取banner成功,条数:${t.size}",Toast.LENGTH_LONG).show()
        })

    }
}