package com.codeseyee.cloydcontact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeseyee.cloydcontact.databinding.FragmentContactsBinding
import com.codeseyee.cloydcontact.ViewModel.ContactsViewModel
import com.codeseyee.cloydcontact.adapters.UserAdapter

class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private val contactsViewModel: ContactsViewModel by lazy { ContactsViewModel() }
    private var userId: String? = null
    private var userType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("USER_ID")
            userType = it.getString("USER_TYPE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        binding.progressBar2.visibility = View.GONE
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe the users LiveData from the ViewModel
        contactsViewModel.users.observe(viewLifecycleOwner, { users ->
            if (users.isEmpty()) {
                binding.empty.visibility = View.VISIBLE
            } else {
                binding.empty.visibility = View.GONE
                // Populate RecyclerView with contacts data
                binding.recyclerView.adapter = UserAdapter(users)
            }
        })

        // Fetch and display contacts based on userType
        fetchContacts()

        return binding.root
    }

    private fun fetchContacts() {
        userType?.let {
            contactsViewModel.fetchUsers(it)
        }
    }
}
