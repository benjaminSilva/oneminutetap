package com.bsoftwares.oneminute.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.databinding.ActivitySettingsBinding
import com.bsoftwares.oneminute.databinding.MaxMinRandomDialogBinding
import com.bsoftwares.oneminute.util.BuzzType
import com.bsoftwares.oneminute.util.buzz
import com.bsoftwares.oneminute.viewModel.SettingsViewModel
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
            MediaPlayer.create(this, R.raw.song1).start()
        })

        viewModel.vibration.observe(this, androidx.lifecycle.Observer {
            buzz(BuzzType.COUNTDOWN_PANIC.pattern, this)
        })

        viewModel.replay.observe(this, Observer {
            Toast.makeText(this, getString(R.string.replay, it), Toast.LENGTH_SHORT).show()
        })

        viewModel.dialog.observe(this) { display ->
            if (display) {
                AlertDialog.Builder(this).apply {
                    val sharedPref = getPreferences(Context.MODE_PRIVATE)
                    val list = listOf(
                        "10",
                        "20",
                        "30",
                        "40",
                        "50",
                        "60",
                        "90",
                        "120",
                        "150",
                        "200",
                        "300",
                        "400",
                        "500"
                    )
                    val dialogBinding =
                        MaxMinRandomDialogBinding.inflate(LayoutInflater.from(this@SettingsActivity))
                    val adapter = ArrayAdapter(
                        this@SettingsActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        list
                    )
                    dialogBinding.spinnerMin.adapter = adapter
                    if (sharedPref.contains("min"))
                        dialogBinding.spinnerMin.setSelection(sharedPref.getInt("min",0))
                    dialogBinding.spinnerMin.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                val newList = list.toMutableList()
                                for (i in 0..position) {
                                    newList.removeFirst()
                                }
                                val adapter2 = ArrayAdapter(
                                    this@SettingsActivity,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    newList
                                )
                                var text = ""
                                if (dialogBinding.spinnerMax.selectedItem != null) {
                                    text = dialogBinding.spinnerMax.selectedItem as String
                                }
                                dialogBinding.spinnerMax.adapter = adapter2
                                if (text.isNotEmpty())
                                    dialogBinding.spinnerMax.setSelection(newList.indexOf(text), true)
                                if (sharedPref.contains("max"))
                                    dialogBinding.spinnerMax.setSelection(sharedPref.getInt("max",0))
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                        }
                    setPositiveButton(R.string.set_min_max_value) { dialogInterface, i ->
                        getPreferences(Context.MODE_PRIVATE).edit().apply {
                            putInt("min",dialogBinding.spinnerMin.selectedItemPosition)
                            putInt("max",dialogBinding.spinnerMax.selectedItemPosition)
                           apply()
                        }
                        val min = dialogBinding.spinnerMin.selectedItem as String
                        val max = dialogBinding.spinnerMax.selectedItem as String
                        viewModel.setMinMax(min.toInt(), max.toInt())
                        dialogInterface.dismiss()
                    }
                    setNegativeButton(R.string.cancel) { dialogInterface, i ->
                        viewModel.turnRandomOff()
                        dialogInterface.dismiss()
                    }
                    setView(dialogBinding.root)
                }.create().apply {
                    setCanceledOnTouchOutside(false)
                    setCancelable(false)
                }.show()
            }
        }

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