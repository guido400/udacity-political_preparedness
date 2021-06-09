package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionRepository
import timber.log.Timber


class MyApplication:Application() {

    var electionRepository: ElectionRepository? = null

    fun getElectionRepositoryInstance ():ElectionRepository {
        if (electionRepository == null) {
            val dao = ElectionDatabase.getInstance(applicationContext).electionDao
            electionRepository = ElectionRepository(dao)
        }
        return electionRepository as ElectionRepository
    }

    override fun onCreate() {
        super.onCreate()
            Timber.plant(Timber.DebugTree());
    }
}