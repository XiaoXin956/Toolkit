package com.xiaoxin.toolkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xiaoxin.toolkit.databinding.ActivityPageSlideBinding

class PageSlideActivity : AppCompatActivity() {

    lateinit var activityPageSlideBinding: ActivityPageSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPageSlideBinding = DataBindingUtil.setContentView(this, R.layout.activity_page_slide)


    }
}