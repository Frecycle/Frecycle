package io.github.frecycle.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SelectionsStatePagerAdapter : FragmentStatePagerAdapter {
    private val fragmentList : ArrayList<Fragment> = ArrayList()
    private val fragments : HashMap<Fragment, Int> = HashMap()
    private val fragmentNumbers : HashMap<String, Int> = HashMap()
    private val fragmentNames : HashMap<Int, String> = HashMap()

    constructor(fm: FragmentManager) : super(fm)

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment : Fragment, fragmentName : String){
        fragmentList.add(fragment)
        fragments[fragment] = fragmentList.size-1
        fragmentNumbers[fragmentName] = fragmentList.size-1
        fragmentNames[fragmentList.size-1] = fragmentName
    }

    fun getFragmentNumber(fragmentName: String): Int? {
        if(fragmentNumbers.containsKey(fragmentName)){
            return fragmentNumbers[fragmentName]
        }else{
            return null
        }
    }

    fun getFragmentNumber(fragment: Fragment):Int?{
        if(fragments.containsKey(fragment)){
            return fragments[fragment]
        }else{
            return null
        }
    }

    fun getFragmentName(fragmentNumber: Int):String?{
        if(fragmentNames.containsKey(fragmentNumber)){
            return fragmentNames[fragmentNumber]
        }else{
            return null
        }
    }

}