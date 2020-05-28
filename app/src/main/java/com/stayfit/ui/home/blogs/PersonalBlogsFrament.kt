package com.stayfit.ui.home.blogs

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stayfit.R


class PersonalBlogsFragment : Fragment() {
    private val TAG = "PersonalBlogsFragment"

    private var addButton: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.personal_blogs_fragment, container, false)

        addButton = root.findViewById<View>(R.id.btn_add_blog) as FloatingActionButton
        addButton!!.setOnClickListener {
            Log.d(TAG, "addButton clicked!")
            showNewNameDialog()
        }

        return root
    }

    /**
     *
     */
    private fun showNewNameDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.create_blog_dialog, null)
        dialogBuilder.setView(dialogView)

        val blogName = dialogView.findViewById<EditText>(R.id.et_blog_name)
        val blogDescription = dialogView.findViewById<EditText>(R.id.et_blog_desc)

        dialogBuilder.setTitle("Custom new blog")
        dialogBuilder.setPositiveButton("Save") { dialog, whichButton ->
            //do something with edt.getText().toString();
            /*
            val payload = hashMapOf(
                "user_id" to mAuth.uid,
                "name" to blogName.text.toString(),
                "description" to blogDescription.text.toString()
            )
            db.collection("blogs")
                .add(payload)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
             */
        }

        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}// Required empty public constructor