package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.representative.model.RepresentativeRepository


class RepresentativeViewModelFactory(private val repository: RepresentativeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}