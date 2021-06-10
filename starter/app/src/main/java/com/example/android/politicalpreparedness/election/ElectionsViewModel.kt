package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.ApiStatus
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val repository: ElectionRepository): ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections= MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections
    val savedElections = repository.savedElections


    private val _apiStatus= MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    init {
        getUpcomingElections()



        viewModelScope.launch {
            if (repository.getElection(1000) == null) {
                repository.saveElection(
                    Election(
                        1000,
                        "test",
                        Date(),
                        Division("1000", "USA", "FL")
                    )
                )
            }
        }
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


    //TODO: Create functions to navigate to saved or upcoming election voter info

}