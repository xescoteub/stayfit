package com.stayfit.ui.myroutines

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_form_routine.*
import java.io.ByteArrayOutputStream


class FormRoutine : AppCompatActivity() {
    val PICK_IMAGE = 1
    val REQUEST_IMAGE_CAPTURE = 3
    var photo: String = "null"
    private lateinit var mAuth: FirebaseAuth
    private var mStorageRef: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_routine)
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance()
    }

    fun addNewRoutine(view: View) {
        val textInputLayoutName = findViewById<TextInputLayout>(R.id.input_name_r)
        val name: String = textInputLayoutName.editText!!.text.toString()
        val textInputLayoutDescription = findViewById<TextInputLayout>(R.id.input_description_r)
        val description: String = textInputLayoutDescription.editText!!.text.toString()
        if (name.equals("")){ input_name_r.error = resources.getString(R.string.invalid_name)}
        else{
            val intent = Intent()
            var arrayList: ArrayList<String> = ArrayList()
            arrayList.add(name)
            arrayList.add(description)
            arrayList.add(photo)
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


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView: ImageView = findViewById(R.id.img_selected)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
            photo = uri.toString()
            fileUploader()
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            photo = getImageUri(this, imageBitmap).toString()
            fileUploader()
        }
    }

    fun startCamera(view: View) {dispatchTakePictureIntent()}
    //convierte el bitmap en Uri
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun fileUploader(){
        val currentUserID = mAuth.currentUser?.uid.toString()
        var pathString = System.currentTimeMillis().toString()+"."+getExtension(photo.toUri())
        val riversRef: StorageReference = mStorageRef!!.child("Backgrounds").child(currentUserID).child(pathString)

        riversRef.putFile(photo.toUri())
            .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                // ...
            }
        photo = pathString
    }

    private fun getExtension(uri: Uri): String{
        var cr:ContentResolver = contentResolver
        var mimeTypeMap:MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri))!!
    }


}
