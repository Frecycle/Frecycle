package io.github.frecycle.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import io.github.frecycle.*

class BottomNavigationViewHelper {

    object Check{
        fun checkMenuItem(bottomNavigationView : BottomNavigationView, activityNumber : Int){
            val menu : Menu =  bottomNavigationView.menu
            val menuItem : MenuItem = menu.getItem(activityNumber)
            menuItem.isChecked = true
        }
    }

    companion object{
        var auth: FirebaseAuth = FirebaseAuth.getInstance()

        fun setupBottomNavigationView(applicationContext: Context, activity : Activity, bottomNavigationView : BottomNavigationView){
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->

                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (activity !is HomeActivity ){
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            applicationContext.startActivity(intent)
                            activity.overridePendingTransition(0,0)
                        }
                    }

                    R.id.navigation_search -> {
                        if(activity !is SearchActivity){
                            val intent = Intent(applicationContext, SearchActivity::class.java)
                            applicationContext.startActivity(intent)
                            activity.overridePendingTransition(0,0)
                        }
                    }

                    R.id.navigation_favorites -> {
                        if(activity !is AccountActivity || activity !is FavoritesActivity ){
                            if(auth.currentUser == null){
                                val intent = Intent(applicationContext, AccountActivity::class.java)
                                intent.putExtra("activityNum","2")
                                applicationContext.startActivity(intent)
                                activity.overridePendingTransition(0,0)
                            }else{
                                val intent = Intent(applicationContext, FavoritesActivity::class.java)
                                applicationContext.startActivity(intent)
                                activity.overridePendingTransition(0,0)
                            }
                        }
                    }

                    R.id.navigation_myAccount -> {
                        if(activity !is AccountActivity || activity !is ProfileActivity){
                            if(auth.currentUser == null){
                                val intent = Intent(applicationContext, AccountActivity::class.java)
                                intent.putExtra("activityNum","3")
                                applicationContext.startActivity(intent)
                                activity.overridePendingTransition(0,0)
                            }else{
                                val intent = Intent(applicationContext, ProfileActivity::class.java)
                                applicationContext.startActivity(intent)
                                activity.overridePendingTransition(0,0)
                            }
                        }
                    }
                }
                true
            }
        }
    }



}