package io.github.frecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.nostra13.universalimageloader.core.ImageLoader
import io.github.frecycle.models.Product
import io.github.frecycle.util.BottomNavigationViewHelper
import io.github.frecycle.util.RecyclerViewAdapter
import io.github.frecycle.util.UniversalImageLoader

class HomeActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var bottomNavigation : BottomNavigationViewEx
    private lateinit var backToast: Toast
    private lateinit var addProductButton : Button


    private var backPressedTime : Long = 0
    private val activityNum : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        bottomNavigation = findViewById(R.id.bottom_nav)

        // BottomNavigationView activity changer
        BottomNavigationViewHelper.setupBottomNavigationView(applicationContext,this, bottomNavigation)

        addProductButton = findViewById(R.id.addProductButton)

        //TODO SİLİNECEK
        addProductButton.setOnClickListener{
            val intent = Intent(applicationContext, ProductActivity::class.java)
            startActivity(intent)
        }

        initImageLoader()
        tryCardView()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    override fun onResume() {
        super.onResume()
        BottomNavigationViewHelper.Check.checkMenuItem(bottomNavigation,activityNum)
    }

    private fun initImageLoader(){
        val universalImageLoader = UniversalImageLoader(applicationContext)
        ImageLoader.getInstance().init(universalImageLoader.getConfig())
    }

    override fun onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel()
            super.onBackPressed()
            return
        }else{
            backToast = Toast.makeText(applicationContext,"Press back again to exit",Toast.LENGTH_SHORT)
            backToast.show()
        }

        backPressedTime = System.currentTimeMillis()
    }

    private fun tryCardView(){
        val list : ArrayList<Product> =  ArrayList()
        val photoList : ArrayList<String> = ArrayList()

        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel,photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel2,photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel3, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel4, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel2, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel3, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel4, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel2, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel, photoList))
        list.add(Product("1","parfume","Chanel N5 Fragrance", "Now its free",R.drawable.chanel3, photoList))


        val recyclerView : RecyclerView = findViewById(R.id.recycleViewHome)
        val recycleViewAdapter : RecyclerViewAdapter = RecyclerViewAdapter(this, list)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = recycleViewAdapter
    }

}
