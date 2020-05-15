package com.stayfit.ui.profile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.stayfit.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.HashMap


class ProfileFragment : Fragment() {
    private lateinit var mAuth : FirebaseAuth


    var km_recorred: Int = 0
    var times_loged: Int = 0

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var view:View = inflater.inflate(R.layout.profile_fragment, container, false)

        datosusuario(view)

        mAuth = FirebaseAuth.getInstance()
        var actualuser = mAuth.currentUser

        // poner datos actualizados del usuario

        var userid = actualuser?.displayName
        var username: TextView = view.findViewById(R.id.profile_username)
        username.setText(userid)

        var email= actualuser?.email
        var useremail: TextView = view.findViewById(R.id.profile_email)
        useremail.setText(email)


        var userphoto: CircleImageView = view.findViewById(R.id.user_image)
        Picasso.get().load(actualuser?.photoUrl).placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(userphoto)
        //


        //GO-TO
       var instabutton: ImageView = view.findViewById(R.id.instaButton)
        instabutton.setOnClickListener { goto_insta() }

        var youtubebutton: ImageView = view.findViewById(R.id.youtubeButton)
        youtubebutton.setOnClickListener { goto_youtube() }



        return view
    }

    private fun datosusuario(view: View) {
        val db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


        db.collection("users").document(mAuth.uid!!)
            .get().addOnSuccessListener { result ->
                val user = result.data

                val email = user?.get("user_email").toString()

                Log.i("users", user?.get("user_email").toString())

                /*for(user_iterator in result){

                    val user = user_iterator.data as HashMap<*, *>
                    Log.i("Users", user["user_email"].toString() )

                    if(user["user_email"].toString().equals(actualuser?.email)){

                        var username1: TextView = view.findViewById(R.id.profile_username)
                        username1.setText(user["user_email"].toString())
                    }


                }*/

            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun goto_insta(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
        startActivity(intent)

    }
    private fun goto_youtube(){

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCSRvhTSRmdqNA-FhGBLbzIQ"))
        startActivity(intent)
    }


}
