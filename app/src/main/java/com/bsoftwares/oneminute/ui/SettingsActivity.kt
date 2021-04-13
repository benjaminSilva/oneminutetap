package com.bsoftwares.oneminute.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.databinding.ActivitySettingsBinding
import com.bsoftwares.oneminute.util.BuzzType
import com.bsoftwares.oneminute.util.buzz
import com.bsoftwares.oneminute.viewModel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(
            this,
            SettingsViewModel.Factory(activity.application)
        ).get(SettingsViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.sound.observe(this, androidx.lifecycle.Observer {
            MediaPlayer.create(this,R.raw.song1).start()
        })

        viewModel.vibration.observe(this, androidx.lifecycle.Observer {
            buzz(BuzzType.COUNTDOWN_PANIC.pattern,this)
        })

        viewModel.replay.observe(this, Observer {
                Toast.makeText(this,getString(R.string.replay,it), Toast.LENGTH_SHORT).show()

        })

        btn_addTime.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.repeatTimerEquation(true)
                    btn_lessTime.isEnabled = false
                }
                MotionEvent.ACTION_UP -> {
                    btn_lessTime.isEnabled = true
                    viewModel.stopRepeat(true)
                }
            }
            return@setOnTouchListener true
        }

        btn_lessTime.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.repeatTimerEquation(false)
                    btn_addTime.isEnabled = false
                }
                MotionEvent.ACTION_UP -> {
                    btn_addTime.isEnabled = true
                    viewModel.stopRepeat(false)
                }
            }
            return@setOnTouchListener true
        }
    }


    fun onBack(v: View) {
        onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}