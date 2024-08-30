package com.codeseyee.cloydcontact.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.codeseyee.cloydcontact.R
import com.codeseyee.cloydcontact.ViewModel.ContactsViewModel
import com.codeseyee.cloydcontact.adapters.GenericAdapter
import com.codeseyee.cloydcontact.databinding.FragmentContactsBinding
import com.codeseyee.cloydcontact.utils.AppUtils

class ContactsFragment : Fragment(), GenericAdapter.MultipleSelectionInterface {

    private lateinit var binding: FragmentContactsBinding
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var adapter: GenericAdapter
    private var selectionView: View? = null
    private var selectionCount = 0
    private var isSelectionMode = false

    private val requestContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                viewModel.refresh()
            } else {
                binding.layoutPermission.visibility = View.VISIBLE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        adapter = GenericAdapter(requireContext(), listOf()).apply {
            setSelectionListener(this@ContactsFragment)
        }
        binding.recyclerView.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        } else {
            viewModel.refresh()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.searchKey.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.refresh(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.contactsList.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
            binding.empty.visibility = if (it.size <= 1) View.VISIBLE else View.GONE
        })

        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.progressBar2.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isSelectionMode.observe(viewLifecycleOwner, Observer { isSelectionMode ->
            if (isSelectionMode) {
                selectionView?.visibility = View.VISIBLE
            } else {
                selectionView?.visibility = View.GONE
            }
        })

        return binding.root
    }

     override fun onEnterSelectionMode() {
        viewModel.enterSelectionMode()
    }

     fun isSelectionMode(): Boolean {
        return viewModel.isSelectionMode.value == true
    }

     override fun onSelectionChanged(item: Any) {
        if (item is Contact) {
            selectionCount += if (item.isSelected) 1 else -1
            selectionView?.findViewById<TextView>(R.id.selection_count)?.text = "$selectionCount contact(s) selected"
        }
    }

    override fun exitSelectionMode(): Boolean {
        viewModel.exitSelectionMode()
        return true
    }

    private fun deleteSelectedContacts() {
        val selectedContacts = (adapter.currentList.filterIsInstance<Contact>()).filter { it.isSelected }
        if (selectedContacts.isEmpty()) {
            viewModel.exitSelectionMode()
            return
        }

        AppUtils.showYesNoDialog(
            requireContext(),
            "Delete selected contacts!",
            "Are you sure you want to delete selected contacts? They will also be deleted from your phonebook."
        ) {
            AppUtils.showProgressDialog(requireContext(), "Deleting", "Deleting contacts, please wait...").apply {
                viewModel.deleteSelectedContacts(selectedContacts)
                dismiss()
                Toast.makeText(requireContext(), "Contacts deleted.", Toast.LENGTH_SHORT).show()
                viewModel.exitSelectionMode()
            }
        }
    }
}
