package com.codeseyee.cloydcontact.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeseyee.cloydcontact.R

class GenericAdapter(
    private val context: Context,
    private var items: List<Any>
) : ListAdapter<Any, GenericAdapter.GenericViewHolder>(DiffCallback) {

    interface MultipleSelectionInterface {
        fun onSelectionChanged(item: Any)
        fun onEnterSelectionMode()
        fun exitSelectionMode(): Boolean
    }

    private var selectionListener: MultipleSelectionInterface? = null
    private val selectedItems = mutableSetOf<Any>()
    private var isSelectionMode = false

    fun setSelectionListener(listener: MultipleSelectionInterface) {
        selectionListener = listener
    }

    fun updateData(newItems: List<Any>) {
        items = newItems
        submitList(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return GenericViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                handleSelection(item)
            } else {
                // Handle normal item click
            }
        }
    }

    private fun handleSelection(item: Any) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        notifyDataSetChanged()
        selectionListener?.onSelectionChanged(item)
    }

    inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.contact_name)

        fun bind(item: Any) {
            when (item) {
                is Contact -> {
                    nameTextView.text = item.displayName
                    itemView.isSelected = selectedItems.contains(item)
                }
                // Handle other item types
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            // Implement logic to check if items are the same
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            // Implement logic to check if item contents are the same
            return oldItem == newItem
        }
    }
}
