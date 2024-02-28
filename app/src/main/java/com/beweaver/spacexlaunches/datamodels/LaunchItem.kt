package com.beweaver.spacexlaunches.datamodels

import com.beweaver.spacexlaunches.PastLaunchesQuery
import com.beweaver.spacexlaunches.utils.DateTimeUtil

data class LaunchItem(val missionName: String,
                      val launchDateLocal: String,
                      val siteName: String,
                      val articleLink: String,
                      val videoLink: String,
                      val rocketName: String) {
    companion object {
        fun fromLaunchesPast(launchesPastList: List<PastLaunchesQuery.LaunchesPast?>): List<LaunchItem> {
            return launchesPastList.mapNotNull { launchesPast ->
                launchesPast?.let {
                    LaunchItem(missionName = launchesPast.mission_name ?: "",
                        launchDateLocal = launchesPast.launch_date_local?.toString()
                            ?.let { it1 -> DateTimeUtil.convertTimestampToReadable(it1) }
                            ?: "",
                        siteName = launchesPast.launch_site?.site_name_long ?: "",
                        articleLink = launchesPast.links?.article_link ?: "",
                        videoLink = launchesPast.links?.video_link ?: "",
                        rocketName = launchesPast.rocket?.rocket_name ?: ""
                    )
                }

            }
        }
    }
}

