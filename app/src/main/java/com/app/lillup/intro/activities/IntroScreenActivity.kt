package com.app.lillup.intro.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.app.lillup.R
import com.app.lillup.intro.adapter.IntroViewAdapter
import com.app.lillup.intro.view_model.IntroViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class IntroScreenActivity : AppCompatActivity(){
    private lateinit var viewModel: IntroViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
        setContentView(com.app.lillup.R.layout.intro_screen_activity)

        val viewPager:ViewPager2=findViewById(R.id.viewPager)
//        val indicator: CircleIndicator3 =findViewById(R.id.indicator)

        viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
//        indicator.setViewPager(viewPager)
        val adapter = IntroViewAdapter(viewModel.imageList,viewModel.headers,viewModel.footList)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Do something when a new page is selected
            }
        })
        val tabLayout = findViewById<TabLayout>(R.id.into_tab_layout)
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager, true
        ) { tab, position -> }
        tabLayoutMediator.attach()

    }
}