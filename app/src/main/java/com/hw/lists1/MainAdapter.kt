package com.hw.lists1

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainAdapter(
    private val screens: MutableSet<String>,
    activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        val screenCategory: String = screens.elementAt(position)
        return CategoryFragment.newInstance(screenCategory)
    }
}


