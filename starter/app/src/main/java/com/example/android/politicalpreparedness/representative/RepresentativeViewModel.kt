package com.example.android.politicalpreparedness.representative

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.ApiStatus
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.representative.model.RepresentativeRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel(private val repository: RepresentativeRepository): ViewModel() {

    var addressLineOne = ObservableField<String?>()
    var addressLineTwo= ObservableField<String?>()
    var city = ObservableField<String?>()
    var zip = ObservableField<String?>()

    var state = MutableLiveData<String>()

    var locationAddress: Address? = null

    val showSnackBarRes = MutableLiveData<Int>()


    private val _apiStatus= MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _representatives= MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives


    fun getRepresentatives () {
        if (validateAddress()) {
            val address = getAddressFromFields()

            viewModelScope.launch {

                _apiStatus.value = ApiStatus.LOADING
                try {
                    _representatives.value = repository.getRepresentatives(address)

                    _apiStatus.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _apiStatus.value = ApiStatus.ERROR
                    Timber.e("Failure: ${e.message}")
                }
            }
        }
    }

    fun getAddressFromGeoLocation () {
        locationAddress?.let {
            addressLineOne.set(it.line1)
            addressLineTwo.set( it.line2)
            city.set(it.city)
            zip.set(it.zip)
            state.value = it.state
        }
    }

    private fun getAddressFromFields ():String {
        return "${addressLineTwo.get()} ${addressLineOne.get()}, ${city.get()}, ${state.value} ${zip.get()}"
    }

    private fun validateAddress ():Boolean {

        if (addressLineOne.get().isNullOrEmpty() ||
            addressLineTwo.get().isNullOrEmpty() ||
            city.get().isNullOrEmpty() ||
            zip.get().isNullOrEmpty() ||
            state.value.isNullOrEmpty()
        ) {
            showSnackBarRes.value = R.string.address_fields_empty_message
            return false
        } else {
            return true
        }
    }

}
