package com.tez.smartnotepad.ui.fragment.content

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentNewContentBinding
import com.tez.smartnotepad.vm.NewContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class NewContentFragment :
    BaseFragmentWithViewModel<FragmentNewContentBinding, NewContentViewModel>(
        FragmentNewContentBinding::inflate
    ) {

    @Inject
    lateinit var sharedPreferences: PrefDataSource

    private lateinit var user: UserModel
    private lateinit var note: NoteModel
    private lateinit var textFromOcrOrVoice: String

    override val viewModel: NewContentViewModel by viewModels()

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

    override fun initContentsOfViews() {
        with(binding) {
            if (textFromOcrOrVoice.isNotEmpty())
                etContent.setText(textFromOcrOrVoice)
        }
    }

    override fun initListener() {
        with(binding) {
            btnContentConfirm.setOnClickListener {
                viewModel.addContent(user.userId, note.noteId, etContent, 2,
                    onSuccess = {
                        activity?.onBackPressed()
                        showMessage("Eklendi")
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
        fun newInstance(withNoteText: String, note: NoteModel) =
            NewContentFragment().apply {
                arguments = Bundle().apply {
                    putString("textFromOcrOrVoiceRecord", withNoteText)
                    putString("selectedNote", Json.encodeToString(note))
                }
            }
    }
}