package com.tez.smartnotepad.ui.fragment.newnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
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
    ): View? {
        return inflater.inflate(R.layout.fragment_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<Button>(R.id.btnSaveNote)
        val etContent = view.findViewById<EditText>(R.id.noteContentText)
        val etNoteTitle = view.findViewById<EditText>(R.id.noteTitleText)

        newNoteViewModel.note.observe(viewLifecycleOwner) {
            note = it
        }

        if (textFromOcrOrVoice.isNotEmpty())
            etContent.setText(textFromOcrOrVoice)

        btnSave.setOnClickListener {
            newNoteViewModel.createContentAndUpdateTitle(
                etNoteTitle,
                etContent,
                2,
                user.userId,
                onSuccess = {
                    showMessage("Not kaydedildi.")
                },
                onError = {
                    showMessage(it)
                })
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
}