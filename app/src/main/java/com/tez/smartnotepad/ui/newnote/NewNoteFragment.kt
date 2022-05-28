package com.tez.smartnotepad.ui.newnote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.tez.smartnotepad.R
import com.tez.smartnotepad.network.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.service.ContentService
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.vm.NewNoteViewModel

class NewNoteFragment : Fragment() {


    private lateinit var user: UserModel

    private lateinit var newNoteViewModel: NewNoteViewModel
    private lateinit var noteRepository: NoteRepository
    private lateinit var contentRepository: ContentRepository

    private lateinit var noteRemoteDataSource: NoteRemoteDataSource
    private lateinit var contentRemoteDataSource: ContentRemoteDataSource

    private lateinit var noteService: NoteService
    private lateinit var contentService: ContentService

    private lateinit var apiClient: ApiClient
    private lateinit var note: NoteModel
    private lateinit var textFromOcrOrVoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            textFromOcrOrVoice = it!!.getString("textFromOcrOrVoiceRecord", "")
        }

        user = UserModel(
            userId = "2",
            mail = "string1",
            password = "string",
            nameSurname = "string",
            null,
            null
        )
        apiClient = ApiClient
        noteService = apiClient.getClient().create(NoteService::class.java)
        contentService = apiClient.getClient().create(ContentService::class.java)

        noteRemoteDataSource = NoteRemoteDataSource(noteService)
        contentRemoteDataSource = ContentRemoteDataSource(contentService)

        noteRepository = NoteRepository(user, noteRemoteDataSource)
        contentRepository = ContentRepository(user, contentRemoteDataSource)

        newNoteViewModel = NewNoteViewModel(noteRepository, contentRepository)
        newNoteViewModel.createEmptyNote(user)
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

        // TODO CLICKED MORE THAN ONCE? CHECK
        btnSave.setOnClickListener {
            newNoteViewModel.createContentAndUpdateTitle(etNoteTitle, etContent, 2,user.userId)
        }
    }

    override fun onDetach() {
        super.onDetach()

        if (this::note.isInitialized)
            if (note.contentsContentDtos.isNullOrEmpty())
                newNoteViewModel.deleteNote(note)
    }

    // TODO YOU SHOULD CHANGE COMPANION OBJECT I GUESS. CHECK HERE. IMPORTANT
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