package com.tez.smartnotepad.ui.fragment.newnote

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentNewNoteBinding
import com.tez.smartnotepad.vm.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewNoteFragment :
    BaseFragmentWithViewModel<FragmentNewNoteBinding, NewNoteViewModel>(
        FragmentNewNoteBinding::inflate
    ) {

    @Inject
    lateinit var sharedPreferences: PrefDataSource
    override val viewModel: NewNoteViewModel by viewModels()

    private lateinit var user: UserModel
    private lateinit var note: NoteModel

    private lateinit var textFromOcrOrVoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            textFromOcrOrVoice = it!!.getString("textFromOcrOrVoiceRecord", "")
        }

        user = sharedPreferences.user!! // :(
        viewModel.createEmptyNote(user)
    }

    override fun initObserver() {
        viewModel.note.observe(viewLifecycleOwner) {
            note = it
        }
    }

    override fun initContentsOfViews() {
        with(binding) {
            if (textFromOcrOrVoice.isNotEmpty()) {
                noteContentText.setText(textFromOcrOrVoice)
            }
        }
    }

    override fun initListener() {
        with(binding) {
            btnSaveNote.setOnClickListener {
                viewModel.createContentAndUpdateTitle(
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

}