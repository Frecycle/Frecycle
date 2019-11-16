package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class ProfileActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var selectionsPagerAdapter: SelectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        selectionsPagerAdapter = SelectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.viewPager)

        selectionsPagerAdapter.addFragment(OnOfferFragment(), "ON OFFER")
        selectionsPagerAdapter.addFragment(RecycledFragment(), "RECYCLED")
        selectionsPagerAdapter.addFragment(CommentsFragment(), "COMMENTS")

        viewPager.adapter = selectionsPagerAdapter



        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)


        val ratingBar : RatingBar = findViewById(R.id.ratingBar)
        ratingBar.rating = 4.5F

    }
}

/*
                       <com.google.android.material.tabs.TabItem
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:id="@+id/onOfferTab"
                            android:text="@string/onOffer">
                        </com.google.android.material.tabs.TabItem>

                        <com.google.android.material.tabs.TabItem
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:id="@+id/recycledTab"
                            android:text="@string/recycled">
                        </com.google.android.material.tabs.TabItem>

                        <com.google.android.material.tabs.TabItem
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="@string/comments">
                        </com.google.android.material.tabs.TabItem>
 */