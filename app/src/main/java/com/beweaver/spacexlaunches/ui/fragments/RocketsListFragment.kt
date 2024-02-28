package com.beweaver.spacexlaunches.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beweaver.spacexlaunches.PastLaunchesQuery
import com.beweaver.spacexlaunches.datamodels.LaunchItem
import com.beweaver.spacexlaunches.databinding.FragmentRocketsListBinding
import com.beweaver.spacexlaunches.datamodels.Status
import com.beweaver.spacexlaunches.viewmodels.RocketsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.fragment.findNavController
import com.beweaver.spacexlaunches.R

@AndroidEntryPoint
class RocketsListFragment: Fragment() {

    private lateinit var binding: FragmentRocketsListBinding

    private val viewModel: RocketsListViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().window.decorView.systemUiVisibility = View.STATUS_BAR_VISIBLE
        binding = FragmentRocketsListBinding.inflate(inflater, container, false)

        binding.listCompose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Scaffold(
                    topBar = { CenterAlignedTopAppBar(title = {Text(getString(R.string.list_of_rockets),
                        color = Color.White,
                        fontWeight = FontWeight.Bold)}, colors = topAppBarColors(
                        containerColor = Color.Blue,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    )  },
                    content = { MaterialTheme {
                        Box(modifier = Modifier
                            .fillMaxSize().padding(top = 50.dp)) {
                            createUIContent()
                        }
                    } }
                )
            }
        }

        return binding.root
    }

    @Composable
    fun createUIContent() {
        var items by remember { mutableStateOf(emptyList<LaunchItem>()) }
        //var isLoading by remember { mutableStateOf(true) }
        var loadingStatus: Status by remember {
            mutableStateOf(Status.LOADING)
        }

        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                viewModel.getPastLaunches().observe(viewLifecycleOwner) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            val list = it.data as List<PastLaunchesQuery.LaunchesPast?>
                            if (list.isNotEmpty()) {
                                items = LaunchItem.fromLaunchesPast(list)
                                Log.d("Rocket list", "Rocket list load successful!")
                            }
                        }
                        Status.FAILURE -> {
                            loadingStatus = Status.FAILURE
                            Log.d("Rocket list", "Rocket list load failed!")
                        }

                        Status.LOADING -> {
                            loadingStatus = Status.LOADING
                            Log.d("Rocket list", "Rocket list loading!")
                        }
                        else -> {
                        }
                    }
                    loadingStatus = it?.status!!
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()) {
            when (loadingStatus) {
                Status.LOADING -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                Status.FAILURE -> {
                    showErrorDialog()
                }
                else -> {
                    allLaunches(items)
                }
            }
        }
    }

    @Composable
    fun allLaunches(launchList: List<LaunchItem>) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
            verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(16.dp)
            ) {
            items(launchList) {launchItem ->
                launchCard(launchItem)
            }
        }
    }

    @Composable
    fun launchCard(launchItem: LaunchItem) {
        Card(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(16.dp).weight(4f)
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = getString(R.string.mission_name), fontWeight = FontWeight.Bold)
                        Text(text = launchItem.missionName)
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = getString(R.string.rocket_name), fontWeight = FontWeight.Bold)
                        Text(text = launchItem.rocketName)
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = getString(R.string.launch_date), fontWeight = FontWeight.Bold)
                        Text(text = launchItem.launchDateLocal)
                    }
                    if (launchItem.siteName.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                        ) {
                            Text(text = getString(R.string.site_name), fontWeight = FontWeight.Bold)
                            Text(text = launchItem.siteName)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .clickable {
                                findNavController().navigate(
                                    RocketsListFragmentDirections
                                        .toRocketDetailsFragment()
                                        .setArticleURL(launchItem.articleLink)
                                )
                            }
                    ) {
                        Text(text = getString(R.string.article), fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Blue,
                            style = TextStyle(textDecoration = TextDecoration.Underline))
                    }
                }

                Column(horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize().weight(1f)
                    .align(alignment = Alignment.CenterVertically)) {
                    Icon(imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(10.dp)
                            .clickable {
                                findNavController().navigate(
                                    RocketsListFragmentDirections
                                        .toRocketVideoFragment()
                                        .setVideoURL(launchItem.videoLink)
                                )
                            },
                        tint = Color.Red)
                }
            }

        }
    }

    @Composable
    fun showErrorDialog() {
        MaterialTheme {
            Column {
                val openDialog = remember { mutableStateOf(true)  }
                val retryCall = remember { mutableStateOf(false)  }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = getString(R.string.error))
                        },
                        text = {
                            Text(getString(R.string.error_message))
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                    retryCall.value = true
                                }) {
                                Text(getString(R.string.retry))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                    requireActivity().finish()
                                }) {
                                Text(getString(R.string.exit))
                            }
                        }
                    )
                }
                if (retryCall.value) {
                   createUIContent()
                }
            }

        }
    }

}