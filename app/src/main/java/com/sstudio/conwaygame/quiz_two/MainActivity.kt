package com.sstudio.conwaygame.quiz_two

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sstudio.conwaygame.R
import com.sstudio.conwaygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MINIMUM_EVOLUTION_PER_SECOND = 1
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Play or pause game
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

        // Control evolution ratio
        binding.ratioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val evolutionRate =  if (progress < MINIMUM_EVOLUTION_PER_SECOND) {
                        seekBar?.progress = MINIMUM_EVOLUTION_PER_SECOND
                        MINIMUM_EVOLUTION_PER_SECOND
                    } else {
                        progress
                    }

                    // Change hint text
                    binding.ratioText.text = getString(R.string.current_evolution_ration, evolutionRate)
                    binding.gameOfLifeView.setEvolutionRate(evolutionRate)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        // Init hint text
        binding.ratioText.text = getString(R.string.current_evolution_ration, resources.getInteger(R.integer.default_evolution_rate))
    }
}