package com.tez.smartnotepad.ui.content

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.service.ContentService
import com.tez.smartnotepad.ui.newnote.NewNoteFragment
import com.tez.smartnotepad.ui.viewnote.ViewNoteFragment
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.vm.NewContentViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NewContentFragment : Fragment() {

    private lateinit var user: UserModel
    private lateinit var note: NoteModel

    private lateinit var newContentViewModel: NewContentViewModel
    private lateinit var contentRepository: ContentRepository
    private lateinit var contentRemoteDataSource: ContentRemoteDataSource
    private lateinit var contentService: ContentService
    private lateinit var apiClient: ApiClient
    private lateinit var textFromOcrOrVoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            textFromOcrOrVoice = it!!.getString("textFromOcrOrVoiceRecord", "")
            note = Json.decodeFromString(it.getString("selectedNote").toString())
        }

        user =
            UserModel(
                userId = "2",
                mail = "string1",
                password = "string",
                nameSurname = "string",
                null,
                null
            )

        apiClient = ApiClient
        contentService = apiClient.getClient().create(ContentService::class.java)
        contentRemoteDataSource = ContentRemoteDataSource(contentService)
        contentRepository = ContentRepository(user, contentRemoteDataSource)
        newContentViewModel = NewContentViewModel(contentRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<Button>(R.id.btnContentConfirm)
        val etContent = view.findViewById<EditText>(R.id.etContent)

        if (textFromOcrOrVoice.isNotEmpty())
            etContent.setText(textFromOcrOrVoice)

        btnSave.setOnClickListener {

            newContentViewModel.addContent(user.userId, note.noteId, etContent, 2,
                onSuccess = {
                    activity?.onBackPressed()
                    Log.e(name(), "Eklendi")
                },
                onError = {
                    Log.e(name(), "Eklenemedi")
                    Log.e(name(), it)
                })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(withNoteText: String, note: NoteModel) =
            NewContentFragment().apply {
                arguments = Bundle().apply {
                    putString("textFromOcrOrVoiceRecord", withNoteText)
                    putString("selectedNote", Json.encodeToString(note))
                }
            }
    }
}