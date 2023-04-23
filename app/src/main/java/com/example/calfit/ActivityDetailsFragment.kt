package com.example.calfit


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.calfit.databinding.FragmentActivityDetailsBinding
import com.example.calfit.model.Subcategory
import com.example.calfit.model.Variant
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.*
import kotlin.math.roundToInt


class ActivityDetailsFragment : Fragment() {
    private var _binding: FragmentActivityDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: ActivityDetailsFragmentArgs by navArgs()
    private var textToSpeech: TextToSpeech? = null
    private lateinit var subCategory: Subcategory
    private lateinit var variant: Variant
    var position: Int = 0
    var started:Boolean=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        variant = args.variants
        subCategory = variant.subcategories?.get(0)!!
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = subCategory.youtube_link
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })
        updateUi()
        lifecycle.addObserver(binding.youtubePlayerView);
        textToSpeech = TextToSpeech(
            requireContext()
        ) { p0 ->
            // if No error is found then only it will run
            if (p0 != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech?.language = Locale.UK
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        binding.tvTitle.text = subCategory.title
        binding.tvDescription.text = subCategory.description
        binding.textView9.text = "1 Exercises | ${subCategory.duration}mins | 320 Calories Burn"
        binding.btnStart.text = "Start"
        binding.txtProgressTimer.text="0"
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object :YouTubePlayerCallback{
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(subCategory.youtube_link.toString(),0f)
                }
            })
        binding.btnStart.setOnClickListener {
            when (binding.btnStart.text.toString()) {
                "Start" -> {
                    if (!started) {
                        textToSpeech!!.speak(
                            " start ${subCategory.title}+${subCategory.duration} seconds",
                            TextToSpeech.QUEUE_FLUSH,
                            null
                        )
                        started = true
                        startTimer()
                    }
                }
                "Next" -> updateUi()
            }


        }
    }

    private fun startTimer() {
        object : CountDownTimer((subCategory.duration?.times(1000))?.toLong()!!, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.txtProgressTimer.text = (millisUntilFinished / 1000).toString()
                binding.progressBarTimer.progress =
                    (((millisUntilFinished / 1000) / subCategory.duration!!.toDouble() * 100).roundToInt())
            }

            // Callback function, fired
            // when the time is up
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                variant.subcategories?.get(position)?.isCompleted = true
                binding.txtProgressTimer.text = "Done"
                textToSpeech!!.speak(
                    "${subCategory.title} completed",
                    TextToSpeech.QUEUE_FLUSH,
                    null
                )
                started=false
                binding.btnStart.text = "Next"
                if (position == variant.subcategories?.size?.minus(1)) {
                    findNavController().popBackStack()
                } else {
                    subCategory = variant.subcategories?.get(position + 1)!!
                }
            }
        }.start()
    }


}