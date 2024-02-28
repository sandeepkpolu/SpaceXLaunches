package com.beweaver.spacexlaunches.ui.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.beweaver.spacexlaunches.utils.StringUtil
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RocketVideoFragment: Fragment() {

    private val args: RocketVideoFragmentArgs by navArgs()
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        return ComposeView(requireContext()).apply {
            setContent {
                Box(modifier = Modifier.fillMaxSize()) {
                    YouTubePlayerScreen(
                        videoUrl = args.videoURL
                    )
                }
            }
        }
    }

    @Composable
    fun YouTubePlayerScreen(videoUrl: String) {
        youTubePlayerView = remember {
            YouTubePlayerView(requireContext()).apply {
                id = View.generateViewId()
            }
        }

        DisposableEffect(videoUrl) {
            lifecycleScope.launch {
                youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        StringUtil.getVideoId(videoUrl)?.let { youTubePlayer.loadVideo(it, 0f) }
                    }

                })
            }
            onDispose {
                youTubePlayerView.release()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { youTubePlayerView })
        }
    }

}