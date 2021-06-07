package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

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