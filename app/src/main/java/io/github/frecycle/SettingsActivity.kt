package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SimpleAdapter
import androidx.viewpager.widget.ViewPager
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.SelectionsStatePagerAdapter

class SettingsActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: SelectionsStatePagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var relativeLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewPager = findViewById(R.id.container)
        relativeLayout = findViewById(R.id.relSettingsLayout)
        setupSettingsList()
        setupFragments()
    }

    private fun setupSettingsList(){
        val listView: ListView = findViewById(R.id.lvAccountSettings)

        val listViewTitle = arrayOf(getString(R.string.edit_profile),getString(R.string.sign_out))
        val listViewImage = arrayOf(R.drawable.ic_edit,R.drawable.ic_signout)
        val listViewDescription = arrayOf(getString(R.string.editProfile_description), getString(R.string.signOut_Description))

        val listViewItemsArray : ArrayList<HashMap<String,String>> = ArrayList()

        for((index, value) in listViewTitle.withIndex()){
            val listItem : HashMap<String,String> = HashMap()
            listItem["listViewItemTitle"] = listViewTitle[index]
            listItem["listViewItemDescription"] = listViewDescription[index]
            listItem["listViewImage"] = listViewImage[index].toString()
            listViewItemsArray.add(listItem)
        }

        val from = arrayOf("listViewImage", "listViewItemTitle", "listViewItemDescription")
        val to = arrayOf(R.id.listViewImage, R.id.listViewItemTitle, R.id.listViewItemDescription).toIntArray()

        val adapter = SimpleAdapter(baseContext,listViewItemsArray,R.layout.snippet_listview_appearance,from,to)

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
