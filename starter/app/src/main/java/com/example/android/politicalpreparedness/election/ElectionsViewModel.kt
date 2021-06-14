package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.ApiStatus
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ElectionsViewModel(private val repository: ElectionRepository): ViewModel() {

    private val _upcomingElections= MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections


    val savedElections = repository.savedElections


    private val _apiStatus= MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    init {
        getUpcomingElections()
    }

    private fun getUpcomingElections () {

        viewModelScope.launch{
            _apiStatus.value = ApiStatus.LOADING
            try {
                _upcomingElections.value = repository.getComingElections()
                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
                Timber.e("Failure: ${e.message}")
            }
        }
    }

}