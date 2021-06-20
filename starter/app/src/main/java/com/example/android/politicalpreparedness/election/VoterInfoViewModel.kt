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

class VoterInfoViewModel(
    private val dataSource: ElectionRepository,
    private val electionId: Int,
    private val division: Division
) : ViewModel() {

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private lateinit var votingLocation: String

    private lateinit var ballotInformation: String


    private val _webLink = MutableLiveData<String>()
    val webLink: LiveData<String>
        get() = _webLink

    private val _savedState = MutableLiveData<Boolean>()
    val savedState: LiveData<Boolean>
        get() = _savedState

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus


    init {
        getVoterInfo()

        viewModelScope.launch {
            _savedState.value = dataSource.getElection(electionId) != null
        }
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                val voterInfoResponse = dataSource.getVoterInfo(electionId, division)

                _election.value = voterInfoResponse.election
                votingLocation =
                    voterInfoResponse.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
                        ?: ""
                ballotInformation =
                    voterInfoResponse.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl ?: ""


                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
                Timber.e("Failure: ${e.message}")
            }
        }
    }

    fun navigateToVotingLocations() {
        _webLink.value = votingLocation
    }

    fun navigateToBallotInformation() {
        _webLink.value = ballotInformation
    }

    fun setFollowStatus() {
        if (savedState.value == false) {
            followElection()
            _savedState.value = true
        } else {
            unfollowElection()
            _savedState.value = false
        }
    }

    private fun followElection() {
        viewModelScope.launch {
            dataSource.saveElection(election.value!!)
            _savedState.value = true
        }
    }

    private fun unfollowElection() {
        viewModelScope.launch {
            if (election.value != null) {
                dataSource.removeElection(election.value!!)
            }
            _savedState.value = false
        }
    }

}