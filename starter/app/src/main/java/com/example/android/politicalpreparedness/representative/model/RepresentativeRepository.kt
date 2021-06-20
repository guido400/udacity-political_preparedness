package com.example.android.politicalpreparedness.representative.model

import com.example.android.politicalpreparedness.network.CivicsApi

class RepresentativeRepository {

    suspend fun getRepresentatives(address:String): List<Representative> {
        val response = CivicsApi.retrofitService.getRepresentatives(address)
        val offices = response.offices
        val officials = response.officials

        return offices.flatMap{ office -> office.getRepresentatives(officials) }
    }

}