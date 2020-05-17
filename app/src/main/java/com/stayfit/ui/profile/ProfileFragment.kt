package com.stayfit.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.stayfit.R
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.util.HashMap


class ProfileFragment : Fragment() {
    private lateinit var mAuth : FirebaseAuth


    val PICK_IMAGE = 1
    val REQUEST_IMAGE_CAPTURE = 3
    var photo: String = "null"

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var view:View = inflater.inflate(R.layout.profile_fragment, container, false)

        datosusuario(view)// pone en pantalla los datos actualizados el usuario

        mAuth = FirebaseAuth.getInstance()
        var actualuser = mAuth.currentUser
        val db = FirebaseFirestore.getInstance();
        val data = HashMap<String, Any>() // hash map para sobre escribir datos



        var userphoto: CircleImageView = view.findViewById(R.id.user_image)
        Picasso.get().load(actualuser?.photoUrl).placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(userphoto)


        //GO-TO
       var instabutton: ImageView = view.findViewById(R.id.instaButton)
        instabutton.setOnClickListener { goto_insta() }

        var youtubebutton: ImageView = view.findViewById(R.id.youtubeButton)
        youtubebutton.setOnClickListener { goto_youtube() }


        //Controlar Height i Weight

        var heigth : TextView = view.findViewById(R.id.textVHeight)
        heigth.setOnClickListener{

            val hadb: AlertDialog.Builder = AlertDialog.Builder(activity)

            val hView:View = getLayoutInflater().inflate(R.layout.profile_change_height,null)
            val height_et: EditText = hView.findViewById(R.id.editTextHeight)

            hadb.setNegativeButton("Cancel", null)
            hadb.setPositiveButton("Ok") { dialog, which ->

                data["user_height"] = height_et.text.toString()
                db.collection("users").document(mAuth.uid!!).update(data)

                heigth.setText(height_et.text)

            }
            hadb.setTitle("Change Height")
            hadb.setView(hView)
            val hdialog:AlertDialog =hadb.create()
            hdialog.show()

        }

        var weight : TextView = view.findViewById(R.id.textVWeight)
        weight.setOnClickListener{

            val wadb: AlertDialog.Builder = AlertDialog.Builder(activity)

            val wView:View = getLayoutInflater().inflate(R.layout.profile_change_width,null)
            val width_et: EditText = wView.findViewById(R.id.editTextWidth)

            wadb.setNegativeButton("Cancel", null)
            wadb.setPositiveButton("Ok") { dialog, which ->

                data["user_weight"] = width_et.text.toString()
                db.collection("users").document(mAuth.uid!!).update(data)

                weight.setText(width_et.text)

            }
            wadb.setTitle("Change Width")
            wadb.setView(wView)
            val wdialog:AlertDialog =wadb.create()
            wdialog.show()

        }



        return view
    }

    //Recoge los datos del usuario actual y los inserta en su correspondiente sitio del perfil
    private fun datosusuario(view: View) {
        val db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        db.collection("users").document(mAuth.uid!!)
            .get().addOnSuccessListener { result ->
                val user = result.data

                val email = user?.get("user_email").toString()
                var useremail: TextView = view.findViewById(R.id.profile_email)
                useremail.setText(email)

                val username = user?.get("user_name").toString()
                var username_tv : TextView = view.findViewById(R.id.profile_username)
                username_tv.setText(username)

                val phone = user?.get("user_phone").toString()
                var phone_tv : TextView = view.findViewById(R.id.profile_phone)
                phone_tv.setText(phone)

                var height = user?.get("user_height").toString()
                var height_tv : TextView = view.findViewById(R.id.textVHeight)
                height_tv.setText(height)

                var weight = user?.get("user_weight").toString()
                var weight_tv : TextView = view.findViewById(R.id.textVWeight)
                weight_tv.setText(weight)

                var bio = user?.get("user_bio").toString()
                var bio_tv : TextView = view.findViewById(R.id.profileBio)
                bio_tv.setText(bio)


                Log.i("userse1", user?.get("user_email").toString())
                Log.i("usersn1", user?.get("user_name").toString())
                Log.i("usersp1", user?.get("user_phone").toString())
                Log.i("usersh1", user?.get("user_height").toString())
                Log.i("usersw1", user?.get("user_width").toString())

            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun goto_insta(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/stayfit_pis/"))
        startActivity(intent)

    }
    private fun goto_youtube(){

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCSRvhTSRmdqNA-FhGBLbzIQ"))
        startActivity(intent)
    }



}
