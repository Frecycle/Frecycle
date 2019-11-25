package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.SelectionsStatePagerAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: SelectionsStatePagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var bottomNavigation : BottomNavigationViewEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewPager = findViewById(R.id.container)
        relativeLayout = findViewById(R.id.relSettingsLayout)
        setupSettingsList()
        setupFragments()
        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)
    }

    private fun setupSettingsList(){
        val listView: ListView = findViewById(R.id.lvAccountSettings)

        val options : ArrayList<String> = ArrayList()
        options.add(getString(R.string.edit_profile))
        options.add(getString(R.string.sign_out))

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id -> setViewPager(position) }
    }

    private fun setViewPager(fragmentNumber : Int){
        relativeLayout.visibility = View.GONE
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = fragmentNumber
    }

    private fun setupFragments(){
        pagerAdapter = SelectionsStatePagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(EditProfileFragment(),getString(R.string.edit_profile))
        pagerAdapter.addFragment(SignOutFragment(),getString(R.string.sign_out))
    }

    fun backToProfileClicked(view : View) {
        finish()
    }

    fun backToSettingsClicked(view : View){
        finish()
        overridePendingTransition(0, 0)
        startActivity(Intent(applicationContext,SettingsActivity::class.java))
        overridePendingTransition(0, 0)
    }

}
