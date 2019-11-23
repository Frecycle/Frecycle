package io.github.frecycle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import io.github.frecycle.util.SelectionsStatePagerAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    private lateinit var pagerAdapter: SelectionsStatePagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var relativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mContext = this
        viewPager = findViewById(R.id.container)
        relativeLayout = findViewById(R.id.relSettingsLayout)
        setupSettingsList()

        setupFragments()
    }

    private fun setupSettingsList(){
        var listView: ListView = findViewById(R.id.lvAccountSettings)

        var options : ArrayList<String> = ArrayList()
        options.add(getString(R.string.edit_profile))
        options.add(getString(R.string.sign_out))

        var adapter = ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options)
        listView.adapter = adapter


        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setViewPager(position)
            }
        })
    }

    private fun setViewPager(fragmentNumber : Int){
        relativeLayout.visibility = View.GONE
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = fragmentNumber
    }

    private fun setupFragments(){
        pagerAdapter = SelectionsStatePagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(EditProfileFragment(),getString(R.string.edit_profile))
        pagerAdapter.addFragment(EditProfileFragment(),getString(R.string.sign_out))
    }

    fun backToProfileClicked(view: View) {
        finish()
    }
}
