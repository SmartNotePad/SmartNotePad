package com.tez.smartnotepad.ui.fragment.newnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentNewNoteBinding
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.vm.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewNoteFragment : Fragment() {


    @Inject
    lateinit var sharedPreferences: PrefDataSource
    val newNoteViewModel: NewNoteViewModel by viewModels()

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: UserModel
    private lateinit var note: NoteModel

    private lateinit var textFromOcrOrVoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            textFromOcrOrVoice = it!!.getString("textFromOcrOrVoiceRecord", "")
        }

        user = sharedPreferences.user!! // :(
        newNoteViewModel.createEmptyNote(user)
        Log.e(name(), user.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            newNoteViewModel.note.observe(viewLifecycleOwner) {
                note = it
            }

            if (textFromOcrOrVoice.isNotEmpty()) {
                noteContentText.setText(textFromOcrOrVoice)
            }

            btnSaveNote.setOnClickListener {
                newNoteViewModel.createContentAndUpdateTitle(
                    noteTitleText,
                    noteContentText,
                    2,
                    user.userId,
                    onSuccess = {
                        showMessage("Not kaydedildi.")
                    },
                    onError = {
                        showMessage(it)
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(withNoteText: String?) =
            NewNoteFragment().apply {
                arguments = Bundle().apply {
                    putString("textFromOcrOrVoiceRecord", withNoteText)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}