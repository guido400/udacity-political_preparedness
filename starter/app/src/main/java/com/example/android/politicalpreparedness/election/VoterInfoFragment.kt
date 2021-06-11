package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val args:VoterInfoFragmentArgs by navArgs()


        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel
        val application =  requireActivity().application as MyApplication

        val viewModelFactory = VoterInfoViewModelFactory(application.getElectionRepositoryInstance(),
            args.argElectionId,
            args.argDivision)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(VoterInfoViewModel::class.java)

        viewModel.webLink.observe(this, Observer { link ->
            openUrlInBrowser(link)
        })

        viewModel.savedState.observe(this, Observer { savedState ->
            binding.button.text = if (savedState) {
                getString(R.string.unfollow_election)
            } else {
                getString(R.string.follow_election)
            }
        })

        viewModel.election.observe(this, Observer { election ->
            binding.electionName.title = election.name
            binding.electionDate.text = election.electionDay.toString()
        })

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    private fun openUrlInBrowser (url:String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        this.startActivity(intent)
    }


}