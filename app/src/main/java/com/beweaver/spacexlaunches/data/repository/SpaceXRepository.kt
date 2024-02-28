package com.beweaver.spacexlaunches.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.beweaver.spacexlaunches.PastLaunchesQuery

import javax.inject.Inject

class SpaceXRepository @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getLaunchPadList(offset: Int, limit: Int) = apolloClient.query(
        PastLaunchesQuery(offset = Optional.presentIfNotNull(offset),
            limit = Optional.presentIfNotNull(limit))
    ).execute()

}