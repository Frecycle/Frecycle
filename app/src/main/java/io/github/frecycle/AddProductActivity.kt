package io.github.frecycle

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddProductActivity : AppCompatActivity() {
    private val GALLERY_PICK = 1
    private var imageUri : Uri? = null
    private lateinit var uploadPhoto : ImageView
    private lateinit var productTitle : EditText
    private lateinit var productDescription : EditText
    private lateinit var productCity: Spinner
    private lateinit var productCategory : Spinner
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var productKey: String
    private lateinit var downloadPhotoURL: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth : FirebaseAuth
    private lateinit var loadingBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        productTitle = findViewById(R.id.productTitle)
        productDescription = findViewById(R.id.productDescription)
        productCity = findViewById(R.id.editCitySpinner)
        productCategory = findViewById(R.id.editCategorySpinner)
        loadingBar = ProgressDialog(this)

        setupProductSpinner()
        setupCitySpinner()
        initializeListeners()
    }

    private fun initializeListeners() {
        val uploadButton : Button = findViewById(R.id.uploadProductButton)
        uploadPhoto = findViewById(R.id.ivAddPhotos)

        uploadPhoto.setOnClickListener ( object : View.OnClickListener{
            override fun onClick(p0: View?) {
                openGallery()
            }
        } )

        uploadButton.setOnClickListener ( object : View.OnClickListener{
            override fun onClick(p0: View?) {
                validateProductData()
            }
        } )

    }

    private fun openGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY_PICK)
    }

    private fun validateProductData(){
        val title = productTitle.text.toString()
        val description = productDescription.text.toString()
        val city = productCity.selectedItem.toString()

        if (imageUri == null || Uri.EMPTY.equals(imageUri)){
            Toast.makeText(this, getString(R.string.NoSelectedPhoto), Toast.LENGTH_LONG).show()
        }else if(TextUtils.isEmpty(title)){
            Toast.makeText(this, getString(R.string.NoProductTitle), Toast.LENGTH_LONG).show()
        }else if(TextUtils.isEmpty(description)) {
            Toast.makeText(this, getString(R.string.NoProductDescription), Toast.LENGTH_LONG).show()
        }else if(city.equals("None")){
            Toast.makeText(this, getString(R.string.NoProductCity), Toast.LENGTH_LONG).show()
        }else{
            storeProductInformation()
        }

    }

    // store image and call database function
    private fun storeProductInformation() {

        loadingBar.setTitle("Adding New Product")
        loadingBar.setMessage("Please wait while we are adding the product...")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()


        val calendar = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.time)

        //actually photo name
        productKey = saveCurrentDate + saveCurrentTime

        val filePath = storageReference.child("products_photos").child(imageUri!!.lastPathSegment + productKey + ".jpg")

        val uploadTask = filePath.putFile(imageUri!!).continueWithTask(object: Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    override fun then(task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                        if (!task.isSuccessful){
                            throw task.exception!!
                        }
                        downloadPhotoURL = filePath.downloadUrl.toString()
                        return filePath.downloadUrl
                    }
                }).addOnCompleteListener(object: OnCompleteListener<Uri>{
                    override fun onComplete(task: Task<Uri>) {
                        if(task.isSuccessful){

                            downloadPhotoURL = task.result.toString()

                            Toast.makeText(this@AddProductActivity, "Photo url saved successfully!", Toast.LENGTH_LONG).show()
                            saveProductInfoToDatabase()
                        }
                    }
                })

    }

    private fun saveProductInfoToDatabase() {
        val productMap : HashMap<String, Any> = HashMap()
        val productId : String = databaseReference.push().key!!

        productMap["category"] = productCategory.selectedItem.toString()
        productMap["city"] = productCity.selectedItem.toString()
        productMap["date"] = saveCurrentDate
        productMap["time"] = saveCurrentTime
        productMap["product_name"] = productTitle.text.toString()
        productMap["description"] = productDescription.text.toString()
        productMap["product_id"] = productId
        productMap["owner"] = auth.currentUser!!.uid
        productMap["state"] = 0

        // products tree
        databaseReference.child("products").child(productId).setValue(productMap).addOnCompleteListener { object: OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    Toast.makeText(this@AddProductActivity,getString(R.string.productIsAdded),Toast.LENGTH_LONG).show()
                    Log.d("AddProductActivity", "product object is added to 'products' database")
                }else{
                    Toast.makeText(this@AddProductActivity,"Error: " + task.exception.toString(),Toast.LENGTH_LONG).show()
                    Log.e("AddProductActivity", "Error: product object cannot be added to 'products' database")
                }
            }
        } }
        // products_photos tree
        val productImagesMap : HashMap<String, Any> = HashMap()

        productImagesMap[productKey] = downloadPhotoURL

        databaseReference.child("products_photos").child(productId).setValue(productImagesMap).addOnCompleteListener { object: OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    Toast.makeText(this@AddProductActivity,getString(R.string.productIsAdded),Toast.LENGTH_LONG).show()
                    Log.d("AddProductActivity", "product image urls is added to 'products_photos' database")
                }else{
                    Toast.makeText(this@AddProductActivity,"Error: " + task.exception.toString(),Toast.LENGTH_LONG).show()
                    Log.e("AddProductActivity", "Error: product image urls cannot be added to 'products_photos' database")
                }
            }
        } }

        // user_products tree
        databaseReference.child("user_products").child(auth.currentUser!!.uid).push().setValue(productId).addOnCompleteListener{ object: OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if(task.isSuccessful){
                    Toast.makeText(this@AddProductActivity,getString(R.string.productIsAdded),Toast.LENGTH_LONG).show()
                    Log.d("AddProductActivity", "product id is added to 'user_products' database")
                }else{
                    Toast.makeText(this@AddProductActivity,"Error: " + task.exception.toString(),Toast.LENGTH_LONG).show()
                    Log.e("AddProductActivity", "Error: product id cannot be added to 'user_products' database")
                }
            }
        } }

        loadingBar.dismiss()
        finish()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK && data != null){
            imageUri = data.data
            uploadPhoto.setImageURI(imageUri)
            uploadPhoto.setBackgroundColor(Color.BLACK)
            uploadPhoto.setPadding(1,1,1,1)
        }
    }

    private fun setupProductSpinner() {
        val categorySpinner: Spinner = findViewById(R.id.editCategorySpinner)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        categorySpinner.adapter = staticAdapter
    }

    private fun setupCitySpinner(){
        val categorySpinner: Spinner = findViewById(R.id.editCitySpinner)
        val staticAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.city_array,
            R.layout.snippet_spinner_item
        )
        staticAdapter.setDropDownViewResource(R.layout.snippet_spinner_dropdown_item)
        categorySpinner.adapter = staticAdapter
    }




}
