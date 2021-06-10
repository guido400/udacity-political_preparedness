package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class ElectionRepository(private val dao: ElectionDao) {
    val savedElections = dao.getAllElections()

    lateinit var x: List<Election>

    suspend fun getComingElections(): List<Election> {
        x = CivicsApi.retrofitService.getElections().elections
        return CivicsApi.retrofitService.getElections().elections
    }

    suspend fun saveElection(election: Election) {
        dao.insert(election)
    }

    suspend fun getElection(id:Int):Election? {
        return dao.get(id)
    }

}



