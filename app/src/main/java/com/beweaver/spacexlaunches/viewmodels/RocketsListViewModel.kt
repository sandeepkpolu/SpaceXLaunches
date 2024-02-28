package com.beweaver.spacexlaunches.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.beweaver.spacexlaunches.data.repository.SpaceXRepository
import com.beweaver.spacexlaunches.datamodels.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RocketsListViewModel @Inject constructor(private val spaceXRepository: SpaceXRepository) : ViewModel() {

    fun getPastLaunches() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val apiResponse = spaceXRepository.getLaunchPadList(0, 10)
            if (!apiResponse.hasErrors()) {
                emit(Resource.success(apiResponse.data?.launchesPast))
            } else {
                emit(Resource.error("Error", apiResponse.errors.toString()))
            }

        } catch (exception: Exception) {
            emit(exception.localizedMessage?.let { Resource.error("Exception", it) })
        }
    }

}