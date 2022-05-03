package com.bsoftwares.oneminute.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.databinding.FragmentTimerBinding
import com.bsoftwares.oneminute.util.BuzzType
import com.bsoftwares.oneminute.util.buzz
import com.bsoftwares.oneminute.viewModel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_timer.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            TimerViewModel.Factory(activity.application)
        ).get(TimerViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentTimerBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.remainingTime.observe(viewLifecycleOwner, Observer {remainingTime ->
            if (viewModel.settings.value!!.vibration){
                when (remainingTime){
                    in 1..5 -> {
                        buzz(BuzzType.COUNTDOWN_PANIC.pattern,activity)
                    }
                    viewModel.max.value!!/2 -> {
                        buzz(BuzzType.HALF_WAY.pattern,activity)
                    }
                    0 -> {
                        buzz(BuzzType.GAME_OVER.pattern,activity)
                    }
                    viewModel.max.value -> {
                        buzz(BuzzType.COUNTDOWN_PANIC.pattern,activity)
                    }
                }
            }
            if (viewModel.settings.value!!.sound){
                when (remainingTime){
                    in 1..5 -> {
                        MediaPlayer.create(context,R.raw.song1).start()
                    }
                    viewModel.max.value!!/2 -> {
                        MediaPlayer.create(context,R.raw.song3).start()
                    }
                    0 -> {
                        MediaPlayer.create(context,R.raw.song2).start()
                    }
                    viewModel.max.value -> {
                        MediaPlayer.create(context,R.raw.song4).start()
                    }
                }
            }
        })

        text_time.setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Settings)
        }
    }

}