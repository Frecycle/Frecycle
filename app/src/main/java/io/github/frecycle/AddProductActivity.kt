package io.github.frecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupProductSpinner()
        setupCitySpinner()
        initializeListeners()
    }

    private fun initializeListeners() {
        val uploadButton : Button = findViewById(R.id.uploadProductButton)

        uploadButton.setOnClickListener ( object : View.OnClickListener{
            override fun onClick(p0: View?) {

            }
        } )

    }

    private fun setupProductSpinner() {
        val categorySpinner: Spinner = findViewById(R.id.editCategory)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        categorySpinner.adapter = staticAdapter
    }

    private fun setupCitySpinner(){
        val categorySpinner: Spinner = findViewById(R.id.editCity)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.city_array,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        categorySpinner.adapter = staticAdapter
    }




}
