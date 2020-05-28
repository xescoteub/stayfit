package com.stayfit.ui.home.blogs

import BlogViewPagerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stayfit.R
import java.util.*


class BlogsActivity : AppCompatActivity() {

    private val TAG = "BlogsActivity"

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_blogs);

        // Toolbar config
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.app_name)
        toolbar.setNavigationOnClickListener { v: View? -> onBackPressed() }

        // Tabs config
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.blogsViewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Personal blogs"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Recommended blogs"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = BlogViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}