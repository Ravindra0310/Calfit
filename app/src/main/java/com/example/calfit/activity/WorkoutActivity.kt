package com.example.calfit.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calfit.databinding.ActivityWorkoutBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WorkoutActivity : AppCompatActivity() {
    private var _binding: ActivityWorkoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val youtubeplayerView=binding.youtubePlayerView


        youtubeplayerView.addYouTubePlayerListener(object :AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val videoId = "S0Q4gqBUs7c"
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
    }

}