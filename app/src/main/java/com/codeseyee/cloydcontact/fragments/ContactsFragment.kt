package com.codeseyee.cloydcontact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeseyee.cloydcontact.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)

        // Hide the progress bar if not needed
        binding.progressBar2.visibility = View.GONE

        // Ensure that other UI components are displayed as intended
        binding.recyclerView.visibility = View.VISIBLE
        binding.searchView.visibility = View.VISIBLE

        return binding.root
    }
}
