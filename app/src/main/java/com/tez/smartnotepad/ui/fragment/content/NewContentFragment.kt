package com.tez.smartnotepad.ui.fragment.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.vm.NewContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class NewContentFragment : Fragment() {

    private lateinit var user: UserModel
    private lateinit var note: NoteModel

    val newContentViewModel: NewContentViewModel by viewModels()

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
                    showMessage("Eklendi")
                },
                onError = {
                    showMessage(it)
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