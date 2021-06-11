package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionRepository(private val dao: ElectionDao) {

    val savedElections = dao.getAllElections()
    var x: VoterInfoResponse? = null

    suspend fun getComingElections(): List<Election> {
        return CivicsApi.retrofitService.getElections().elections
    }

    suspend fun saveElection(election: Election) {
        dao.insert(election)
    }

    suspend fun getElection(id:Int):Election? {
        return dao.get(id)
    }

    suspend fun getVoterInfo(electionId: Int, division: Division): VoterInfoResponse {
        val state = division.state

       if (division.state == "") {
            x = CivicsApi.retrofitService.getVoterInfo(electionId, "fl")
           val y = 1
        } else {
            x = CivicsApi.retrofitService.getVoterInfo(electionId, state)

        }

        return x as VoterInfoResponse
    }

}



