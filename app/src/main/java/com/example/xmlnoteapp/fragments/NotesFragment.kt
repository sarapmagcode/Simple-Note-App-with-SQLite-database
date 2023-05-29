package com.example.xmlnoteapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xmlnoteapp.NoteApplication
import com.example.xmlnoteapp.adapters.NotesListAdapter
import com.example.xmlnoteapp.databinding.FragmentNotesBinding
import com.example.xmlnoteapp.viewmodels.NoteViewModel
import com.example.xmlnoteapp.viewmodels.NoteViewModelFactory
import java.util.Date

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    // Don't forget to put 'name' attribute (application) in AndroidManifest.xml
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Notes list
        val adapter = NotesListAdapter { selectedNoted ->
            val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(selectedNoted.id)
            this.findNavController().navigate(action)
        }
        binding.notesList.adapter = adapter
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it.sortedByDescending { note -> Date(note.noteTimestamp) }) {
                    binding.notesList.scrollToPosition(0)
                }
            }
        }
        binding.notesList.layoutManager = LinearLayoutManager(this.context)

        // Add note button
        binding.addNote.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(-1);
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}