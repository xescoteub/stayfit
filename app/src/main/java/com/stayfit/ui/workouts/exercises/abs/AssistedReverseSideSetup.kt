package com.stayfit.ui.workouts.exercises.abs

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import com.stayfit.R

class AssistedReverseSideSetup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assisted_reverse_side_setup)

        val videoView = findViewById<VideoView>(R.id.videoAssistedReverseSideSetup)
        val path = "android.resource://" + getPackageName() + "/" + R.raw.assisted_reverse_side_situp
        val uri = Uri.parse(path)
        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }
}
