package com.raflisalam.weatherapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raflisalam.weatherapp.ui.fragment.Content1Fragment
import com.raflisalam.weatherapp.ui.fragment.Content2Fragment

class ContentFragmentAdapter(activity: AppCompatActivity, private val location: String) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return Content2Fragment.newInstance(position, location)
        }
        return Content1Fragment.newInstance(position, location)
    }
}