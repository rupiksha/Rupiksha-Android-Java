package com.app.rupiksha.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.rupiksha.R
import com.app.rupiksha.models.IntroModal

class IntroPagerAdapter(private val activity: Activity, private val list: List<IntroModal>) : PagerAdapter() {

    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.onboarding_screen_1, container, false)

        when (position) {
            1 -> view = layoutInflater.inflate(R.layout.onboarding_screen_2, container, false)
            2 -> view = layoutInflater.inflate(R.layout.onboarding_screen_3, container, false)
        }
        
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
