package com.sstudio.conwaygame.quiz_two

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sstudio.conwaygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.setOnClickListener {
            val isPlaying = binding.gameOfLifeView.isPlaying

            // Change play button icon
            binding.playButton.icon = if (isPlaying) {
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_play)
            } else {
                ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause)
            }

            // Change game state
            if (isPlaying) {
                binding.gameOfLifeView.pause()
            } else {
                binding.gameOfLifeView.play()
            }
        }
    }
}