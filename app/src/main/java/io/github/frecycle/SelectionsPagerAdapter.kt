package io.github.frecycle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SelectionsPagerAdapter : FragmentPagerAdapter {

    private val fragmentList : ArrayList<Fragment> = ArrayList()
    private val fragmentTitleList : ArrayList<String> = ArrayList()


    constructor(fm: FragmentManager) : super(fm)


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment:Fragment, title:String){
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}