package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.ApiStatus
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class VoterInfoViewModel(private val dataSource: ElectionRepository, private val electionId:Int, private val division: Division) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _votingLocation = MutableLiveData<String>()
    val votingLocation: LiveData<String>
        get() = _votingLocation

    private val _ballotInformation = MutableLiveData<String>()
    val ballotInformation: LiveData<String>
        get() = _ballotInformation

    private val _webLink= MutableLiveData<String>()
    val webLink: LiveData<String>
        get() = _webLink

    private val _savedState= MutableLiveData<Boolean>()
    val savedState: LiveData<Boolean>
        get() = _savedState

    private val _apiStatus= MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus


    init {
        getVoterInfo()

        viewModelScope.launch {
            _savedState.value = dataSource.getElection(electionId) != null
        }
    }

    //TODO: Add var and methods to populate voter info
    private fun getVoterInfo() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                val voterInfoResponse = dataSource.getVoterInfo(electionId, division)

                _election.value = voterInfoResponse.election
                _votingLocation.value = voterInfoResponse.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
                _ballotInformation.value = voterInfoResponse.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl


                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
                Timber.e("Failure: ${e.message}")
            }
        }
    }


    //TODO: Add var and methods to support loading URLs
    fun navigateToVotingLocations (view: View){

    }

    fun navigateToBallotInformation (view: View){

    }


    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    fun getSavedState():Int {
        return R.string.follow_election
    }
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}