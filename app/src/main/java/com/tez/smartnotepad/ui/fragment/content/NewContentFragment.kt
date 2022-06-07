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
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentNewNoteBinding
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.vm.NewContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class NewContentFragment : Fragment() {


    @Inject
    lateinit var sharedPreferences: PrefDataSource

    private lateinit var user: UserModel
    private lateinit var note: NoteModel

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    val newContentViewModel: NewContentViewModel by viewModels()

    private lateinit var textFromOcrOrVoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            textFromOcrOrVoice = it!!.getString("textFromOcrOrVoiceRecord", "")
            note = Json.decodeFromString(it.getString("selectedNote").toString())
        }

        sharedPreferences.user?.let {
            this.user = it
        }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}