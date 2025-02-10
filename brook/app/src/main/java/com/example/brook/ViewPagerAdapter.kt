package com.example.brook

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return ProfilePageFragment()
            1 -> return AddNewRecipeFragment()
            3 -> return RecipeListFragment()
            else -> return RecipeListFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}
