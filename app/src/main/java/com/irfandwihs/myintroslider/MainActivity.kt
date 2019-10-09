package com.irfandwihs.myintroslider

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import java.text.FieldPosition

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mPager : ViewPager

    var layouts : IntArray = intArrayOf(R.layout.first_slide,R.layout.second_slide,R.layout.third_slide)

    lateinit var dotsLayout: LinearLayout
    lateinit var dots : Array<ImageView>

    lateinit var mAdapter : PageAdapter

    lateinit var btnNext : Button
    lateinit var btnSkip : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PrefManager(this).checkPreference()){
            loadHome()
        }

        if (Build.VERSION.SDK_INT>= 19){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setContentView(R.layout.activity_main)
        mPager = findViewById(R.id.pager) as ViewPager
        mAdapter = PageAdapter(layouts,this)
        mPager.adapter = mAdapter
        dotsLayout = findViewById(R.id.dots) as LinearLayout
        btnSkip = findViewById(R.id.btnSkip) as Button
        btnNext = findViewById(R.id.btnNext) as Button
        btnSkip.setOnClickListener(this)
        btnNext.setOnClickListener(this)

        createDots(0)
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                createDots(position)

                if (position == layouts.size - 1){
                    btnNext.setText("Start")
                    btnSkip.visibility = View.INVISIBLE
                }else{
                    btnNext.setText("Next")
                    btnSkip.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSkip -> {
                loadHome()
                PrefManager(this).writeSP()
            }

            R.id.btnNext -> {
                loadNextSlide()
            }
        }
    }

    private fun loadNextSlide() {
        var nextSlide : Int = mPager.currentItem + 1

        if (nextSlide < layouts.size){
            mPager.setCurrentItem(nextSlide)
        }else{
            loadHome()
            PrefManager(this).writeSP()
        }
    }

    private fun loadHome() {
        startActivity(Intent(this,DashboardActivity::class.java))
    }

    fun createDots(position: Int)
    {
        if (dotsLayout!=null)
        {
            dotsLayout.removeAllViews()
        }
        dots = Array(layouts.size,{i -> ImageView(this)})
        for (i in 0..layouts.size - 1)
        {
            dots[i] = ImageView(this)
            if (i == position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots))
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dots))
            }

            var params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

            params.setMargins(4,0,4,0)
            dotsLayout.addView(dots[i],params)
        }
    }
}
