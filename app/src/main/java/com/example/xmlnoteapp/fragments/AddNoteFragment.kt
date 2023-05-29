package com.example.xmlnoteapp.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.xmlnoteapp.NoteApplication
import com.example.xmlnoteapp.R
import com.example.xmlnoteapp.data.Note
import com.example.xmlnoteapp.databinding.FragmentAddNoteBinding
import com.example.xmlnoteapp.viewmodels.NoteViewModel
import com.example.xmlnoteapp.viewmodels.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddNoteFragmentArgs by navArgs()

    // Don't forget to put 'name' attribute (application) in AndroidManifest.xml
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.noteTitle.text.toString(),
            binding.noteContent.text.toString()
        )
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun getDateInString(): String {
        val date = getCurrentDateTime()
        return date.toString("yyyy/MM/dd HH:mm:ss")
    }

    private fun addNewNote() {
        if (isEntryValid()) {
            viewModel.addNewNote(
                binding.noteTitle.text.toString(),
                binding.noteContent.text.toString(),
                getDateInString()
            )

            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            findNavController().navigate(action)

            binding.status.visibility = View.GONE
        } else {
            binding.status.visibility = View.VISIBLE
        }
    }

    private fun updateNote() {
        if (isEntryValid()) {
            viewModel.updateNote(
                this.navigationArgs.noteId,
                this.binding.noteTitle.text.toString(),
                this.binding.noteContent.text.toString(),
                getDateInString()
            )

            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            findNavController().navigate(action)
        } else {
            binding.status.visibility = View.VISIBLE
        }
    }

    private fun bind(note: Note) {
        binding.apply {
            noteTitle.setText(note.noteTitle, TextView.BufferType.SPANNABLE)
            noteContent.setText(note.noteContent, TextView.BufferType.SPANNABLE)

            binding.saveButton.setOnClickListener { updateNote() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.noteId
        if (id != -1) {
            viewModel.getSpecificNote(id).observe(viewLifecycleOwner) { selectedNote ->
                note = selectedNote
                bind(note)
            }

            viewModel.viewNoteOnly()

            // Delete note
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener { showConfirmationDeleteDialog() }
        } else {
            binding.saveButton.setOnClickListener { addNewNote() }
        }

        binding.cancelButton.setOnClickListener {
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun showConfirmationDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString((R.string.no))) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .show()
    }

    private fun deleteNote() {
        viewModel.deleteNote(note)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Hide keyboard
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

        _binding = null
    }
}