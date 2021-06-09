package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter

class ElectionsFragment: Fragment() {

    lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application =  requireActivity().application as MyApplication
        val viewModelFactory = ElectionsViewModelFactory(application.getElectionRepositoryInstance())
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ElectionsViewModel::class.java)

        binding.viewModel = viewModel

        val adapterSaved = ElectionListAdapter(ElectionListener { election ->
            this.findNavController().navigate(ElectionsFragmentDirections
                .actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
            )
        })

        val adapterUpcoming = ElectionListAdapter(ElectionListener { election ->
            this.findNavController().navigate(ElectionsFragmentDirections
                .actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
            )
        })

        binding.recyclerviewSaved.adapter = adapterSaved
        binding.recyclerviewUpcoming.adapter = adapterUpcoming

        //TODO: Link elections to voter info


        viewModel.savedElections.observe(viewLifecycleOwner, Observer {  it?.let {
            adapterSaved.submitList(it)
        }
        })

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {  it?.let {
            adapterUpcoming.submitList(it)
        }
        })


        return binding.root
    }



}