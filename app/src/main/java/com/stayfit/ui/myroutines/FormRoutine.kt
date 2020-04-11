package com.stayfit.ui.myroutines

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stayfit.R


class FormRoutine : AppCompatActivity() {
    val PICK_IMAGE = 1
    var photo: Int ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_routine)
    }

    fun addNewRoutine(view: View) {
        var name: String = findViewById<EditText>(R.id.input_name_r).text.toString()
        var description: String = findViewById<EditText>(R.id.input_description_r).text.toString()
        if (name.equals("")){
            Toast.makeText(this, "You have forgotten to put exercise name.", Toast.LENGTH_SHORT).show()}
        else{
            val intent = Intent()
            var arrayList: ArrayList<String> = ArrayList()
            arrayList.add(name)
            arrayList.add(description)
            if (photo!=null){arrayList.add(photo.toString())}
            else{arrayList.add(R.drawable.blog_5.toString())}
            intent.putExtra("LIST ROUTINE",arrayList)
            setResult(3,intent)
            finish()
        }
    }
    fun returnMyRoutines(view: View) {
        val intent = Intent()
        setResult(0,intent)
        finish()
    }

    fun selectImage(view: View) {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        startActivityForResult(chooserIntent, PICK_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            val imageView: ImageView = findViewById(R.id.img_selected)
            val selectedImage = data.data
            imageView.setImageURI(selectedImage)
            //photo = data.data.toString().split("/")[-1].toInt()
            photo = imageView.imageAlpha
        }
    }

}
