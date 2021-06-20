package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    lateinit var voterInfoViewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args: VoterInfoFragmentArgs by navArgs()

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireActivity().application as MyApplication

        val viewModelFactory = VoterInfoViewModelFactory(
            application.getElectionRepositoryInstance(),
            args.argElectionId,
            args.argDivision
        )

        voterInfoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(VoterInfoViewModel::class.java)

        binding.viewModel = voterInfoViewModel

        voterInfoViewModel.webLink.observe(this, Observer { link ->
            openUrlInBrowser(link)
        })

        voterInfoViewModel.savedState.observe(this, Observer { savedState ->
            binding.button.text = if (savedState) {
                getString(R.string.unfollow_election)
            } else {
                getString(R.string.follow_election)
            }
        })

        voterInfoViewModel.election.observe(this, Observer { election ->
            binding.electionName.title = election.name
            binding.electionDate.text = election.electionDay.toString()
        })


        return binding.root
    }

    /**
     * Open url in browser
     *
     * @param url
     */
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        this.startActivity(intent)
    }


}