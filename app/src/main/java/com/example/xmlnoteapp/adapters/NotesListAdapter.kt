package com.example.xmlnoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlnoteapp.data.Note
import com.example.xmlnoteapp.databinding.NotesListItemBinding

class NotesListAdapter(
    private val onItemClicked: (Note) -> Unit
) : ListAdapter<Note, NotesListAdapter.NotesViewHolder>(DiffCallback) {

    class NotesViewHolder(private var binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                noteTitle.text = note.noteTitle
                noteContent.text = note.noteContent
                noteTimestamp.text = note.noteTimestamp
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NotesListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(current) }
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.noteTitle == newItem.noteTitle
            }
        }
    }
}