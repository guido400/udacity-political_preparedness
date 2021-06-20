package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.RepresentativeRepository
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*

/**
 * Detail fragment
 *
 * @constructor Create empty Detail fragment
 */
class RepresentativeFragment : Fragment(), AdapterView.OnItemSelectedListener {


    lateinit var binding: FragmentRepresentativeBinding

    companion object {
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    }


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private lateinit var representativeViewModel: RepresentativeViewModel

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val stateAdapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.states,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }

        val viewModelFactory = RepresentativeViewModelFactory(RepresentativeRepository())
        representativeViewModel = ViewModelProvider(this, viewModelFactory)
            .get(RepresentativeViewModel::class.java)
        binding.viewModel = representativeViewModel

        representativeViewModel.showSnackBarRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(
                    binding.motionLayout,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        representativeViewModel.state.observe(viewLifecycleOwner, Observer {
            it?.let {
                val states = this.resources.getStringArray(R.array.states)
                val position = states.asList().indexOf(it)
                binding.state.setSelection(position)
            }

        })

        binding.state.adapter = stateAdapter
        binding.state.onItemSelectedListener = this

        binding.buttonLocation.setOnClickListener {
            if (!isPermissionGranted()) {
                representativeViewModel.showSnackBarRes.value =
                    R.string.grant_location_permission_for_this_functionality
            } else {
                getLocation()
            }
        }
        binding.buttonSearch.setOnClickListener {
            representativeViewModel.getRepresentatives()
        }

        val listAdapter = RepresentativeListAdapter()
        binding.representativeList.adapter = listAdapter

        representativeViewModel.representatives.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermissions()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
            }
        }

        return binding.root
    }

    /**
     * On resume
     *
     */
    override fun onResume() {
        super.onResume()
        if (isPermissionGranted()) startLocationUpdates()
    }

    /**
     * On pause
     *
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    /**
     * On request permissions result: if no permission show snackbar else start location updates
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_TURN_DEVICE_LOCATION_ON -> {
                if (grantResults.isEmpty() ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED
                ) {
                    representativeViewModel.showSnackBarRes.value =
                        R.string.location_search_not_be_used
                } else {
                    startLocationUpdates()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    /**
     * Check location permissions
     *
     * @return
     */
    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_TURN_DEVICE_LOCATION_ON
            )
            false
        }
    }

    /**
     * Is permission granted
     *
     * @return
     */
    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get location from fused client, then send address components to edit text fields via viewmodel
     *
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (isPermissionGranted()) {

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )


            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val address = location?.let { geoCodeLocation(it) }
                    representativeViewModel.locationAddress = address
                    representativeViewModel.getAddressFromGeoLocation()
                }
                .addOnFailureListener { Timber.e(it.localizedMessage) }
        }
    }

    /**
     * Create Address instance from geo location
     *
     * @param location
     * @return
     */
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    /**
     * On item selected
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            representativeViewModel.state.value = parent.getItemAtPosition(position).toString()
        }
    }

    /**
     * On nothing selected
     *
     * @param parent
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        //empty method
    }

    /**
     * Start location updates
     *
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (isPermissionGranted()) {
            locationRequest = LocationRequest.create()?.apply {
                interval = 0
                fastestInterval = 0
                numUpdates = 1
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }!!


            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    /**
     * Stop location updates
     *
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}