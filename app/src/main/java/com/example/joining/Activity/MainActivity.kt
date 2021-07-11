package com.example.joining.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.joining.Adapter.ItemAdapter
import com.example.joining.Models.Category
import com.example.joining.R
import com.example.joining.ViewModels.Mvvm
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener, ItemAdapter.Select {
    private lateinit var recyclerView : RecyclerView
    private lateinit var dateTV : TextView
    private lateinit var descET : EditText
    private lateinit var nameET : EditText
    private lateinit var categorySpinner : Spinner
    private lateinit var category : String
    private lateinit var name : String
    private lateinit var desc : String
    private lateinit var date : String
    private lateinit var mvvm: Mvvm
    private lateinit var itemAdapter: ItemAdapter
    private var mResults: java.util.ArrayList<String> = ArrayList()
    private var mResults1: java.util.ArrayList<Image> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mvvm = ViewModelProviders.of(this@MainActivity).get(Mvvm::class.java)
        findIds()
//        getCategoryApi()
    }




    private fun findIds() {
        findViewById<Button>(R.id.saveBTN).setOnClickListener(this)
        findViewById<Button>(R.id.addImagesBTN).setOnClickListener(this)
        findViewById<ImageView>(R.id.dateIV).setOnClickListener(this)
        nameET = findViewById(R.id.nameET)
        descET = findViewById(R.id.descET)
        dateTV = findViewById(R.id.dateTV)
        categorySpinner = findViewById(R.id.categorySpinner)
        recyclerView = findViewById(R.id.recyclerView)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveBTN ->{
                validate()
            }
            R.id.addImagesBTN ->{
                MultipleImageGallery(1,mResults1)
            }
            R.id.dateIV ->{
                clickDataPicker()
            }
        }
    }

    private fun validate() {
        name = nameET.text.toString().trim()
        desc = descET.text.toString().trim()
        date = dateTV.text.toString().trim()

        when{
            category.isEmpty()->{
                Toast.makeText(this, "Please Select Category", Toast.LENGTH_LONG).show()
            }
            name.isEmpty()->{
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show()
            }
            desc.isEmpty()->{
                Toast.makeText(this, "Please Enter Description", Toast.LENGTH_LONG).show()
            }
            date.isEmpty()->{
                Toast.makeText(this, "Please Select Expiry Date", Toast.LENGTH_LONG).show()
            }
            mResults.isEmpty()->{
                Toast.makeText(this, "Please Select Images", Toast.LENGTH_LONG).show()
            }

            else->{
//                saveData()
//                Toast.makeText(this, "Validate", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun clickDataPicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            dateTV.text ="""$year:${monthOfYear + 1}:$dayOfMonth """
//            Toast.makeText(this, """$dayOfMonth / ${monthOfYear + 1} / $year""", Toast.LENGTH_LONG).show()

        }, year, month, day)
        dpd.show()
    }

    private fun getCategoryApi() {

        mvvm.getData(this).observe(this, androidx.lifecycle.Observer {
            if (it.status=="success"){
                setSpinner(it.categories as ArrayList<Category>)
            }
        } )
    }

    private fun setSpinner(result: ArrayList<Category>) {
        categorySpinner.adapter = object : ArrayAdapter<Category>(
            this,
            android.R.layout.simple_spinner_item,
            result
        ) {
//            var typeface = ResourcesCompat.getFont(this@MainActivity)
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val label = super.getView(position, convertView, parent) as TextView
//                label.setTypeface(typeface)
                label.text = getItem(position)?.name
                return label
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val label = super.getView(position, convertView, parent) as TextView
//                label.setTypeface(typeface)
                label.text = getItem(position)?.name
                return label
            }
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category = result.get(position).id
            }
        }
    }

    private fun saveData() {

        val body: ArrayList<MultipartBody.Part> = ArrayList()
        for (i in 0 until mResults.size){
            val file = File(mResults[i])
            val requestFile: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            body.add(MultipartBody.Part.createFormData("product_image["+i+"]", file.name, requestFile))
        }
        val RQ_category: RequestBody = category.toRequestBody("text/plain".toMediaType())
        val RQ_name: RequestBody = name.toRequestBody("text/plain".toMediaType())
        val RQ_desc: RequestBody = desc.toRequestBody("text/plain".toMediaType())
        val RQ_date: RequestBody = date.toRequestBody("text/plain".toMediaType())
        mvvm.saveData(this, RQ_category,RQ_name, RQ_desc, RQ_date,  body).observe(this, androidx.lifecycle.Observer {
            if(it.status=="success"){
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    open fun MultipleImageGallery(code:Int,mResults1:ArrayList<Image>){
        ImagePicker.with(this)
                .setFolderMode(true)
                .setShowCamera(true)
                .setFolderTitle("Album")
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(10)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(code)
                .setSelectedImages(mResults1)
                .start();
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 1)) {
            recyclerView.visibility = View.VISIBLE
            mResults1 = ImagePicker.getImages(data)
            mResults.clear()
            for (image in mResults1) {
                Log.e("DATA",image.path)
                mResults.add(image.path)
            }
            setImages(mResults)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setImages(mResults: ArrayList<String>) {
        itemAdapter = ItemAdapter(this, mResults, this)
        recyclerView.adapter = itemAdapter
    }

    override fun delete(position: Int) {
        mResults.removeAt(position)
        mResults1.removeAt(position)
        itemAdapter.notifyDataSetChanged()
    }
}