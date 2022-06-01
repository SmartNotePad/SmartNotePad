package com.tez.smartnotepad.ui.fragment.viewnote

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.common.InputImage
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ParticipantModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import com.tez.smartnotepad.databinding.FragmentViewNoteBinding
import com.tez.smartnotepad.ui.adapter.content.ContentAdapter
import com.tez.smartnotepad.ui.fragment.content.NewContentFragment
import com.tez.smartnotepad.ui.fragment.dialog.GeneralDialogFragment
import com.tez.smartnotepad.ui.fragment.dialog.ListDialogFragment
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.util.ext.showMessage
import com.tez.smartnotepad.util.ocr.OcrUtils
import com.tez.smartnotepad.vm.ViewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

@AndroidEntryPoint
class ViewNoteFragment : Fragment() {

    private lateinit var note: NoteModel
    private lateinit var binding: FragmentViewNoteBinding
    val viewNoteViewModel: ViewNoteViewModel by viewModels()

    private lateinit var contentAdapter: ContentAdapter
    private lateinit var contents: MutableList<ContentModel>

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(activity) {
            textToSpeechEngine.language = Locale.ENGLISH
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = Json.decodeFromString(it.getString("selectedNote").toString())
        }
        contents = note.contentsContentDtos!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentViewNoteBinding.bind(view)

        contentAdapter = initAdapter()

        with(binding) {
            tvNoteTitle.text = note.title

            rvNoteContents.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvNoteContents.setHasFixedSize(true) // not sure because of xml item height

            rvNoteContents.adapter = contentAdapter

            btnAddContentText.setOnClickListener {
                goNewContentFragment("")
            }

            btnAddContentVoice.setOnClickListener {
                displaySpeechRecognizer()
            }

            btnAddContentCamera.setOnClickListener {
                displayOcr()
            }

            btnShareNote.setOnClickListener {
                showShareDialog(onPositive = { mail ->
                    shareNote(mail)
                })
            }

            btnListen.setOnClickListener {
                speak(contents.toString())
            }

            btnRemoveParticipant.setOnClickListener {
                showRemoveParticipantDialog()
            }
        }
    }


    private fun initAdapter(): ContentAdapter {
        return ContentAdapter(
            contents,
            onEditClickListener = { position ->
                showEditDialog(
                    this.context,
                    onPositive = {
                        val changed = this.copy(context = it)
                        updateContent(position, changed)
                    })
            },
            onDeleteClickListener = { position ->
                deleteContent(position, this)
            })
    }

    private fun updateContent(position: Int, changedContent: ContentModel) {
        viewNoteViewModel.updateContent(changedContent, {
            contents[position] = it
            contentAdapter.notifyItemChanged(position)
            showMessage("Updated.")
        }, { error ->
            showMessage(error)
            Log.e(name(), error)
        })
    }

    private fun deleteContent(position: Int, content: ContentModel) {
        viewNoteViewModel.deleteContent(content, {
            contents.removeAt(position)
            contentAdapter.notifyItemRemoved(position)
            showMessage("Deleted.")
            Log.e("Silindi. Run Delete ($position)", content.context)
        }, {
            showMessage(it)
            Log.e("Content silinirken Hata", it)
        })
    }

    private fun shareNote(mail: String) {
        viewNoteViewModel.shareNote(ShareNoteModel(note.userUserId, note.noteId, mail),
            {
                showMessage("Paylaşıldı.")
                Log.e(name(), it.toString())
            }, {
                showMessage(it)
                Log.e(name(), it)
            })
    }

    private fun showEditDialog(oldValue: String, onPositive: (value: String) -> Unit) {
        val dialogFragment = GeneralDialogFragment(oldValue) { onPositive.invoke(it) }
        dialogFragment.show(childFragmentManager, "Edit")
    }

    private fun showShareDialog(onPositive: (value: String) -> Unit) {
        val dialogFragment = GeneralDialogFragment(null) { onPositive.invoke(it) }
        dialogFragment.show(childFragmentManager, "Share")
    }

    private fun showRemoveParticipantDialog() {

        val participant = ParticipantModel(note.userUserId.toString(), "", note.noteId.toString())

        val dialogFragment = note.participantUsersUserId?.let { participantUsers ->
            ListDialogFragment(participantUsers) {
                viewNoteViewModel.removeParticipantUser(participant.copy(participantUsersUserId = it.userId))
            }
        }

        dialogFragment?.show(childFragmentManager, "RemoveParticipant")
    }

    private fun displaySpeechRecognizer() {
        startForSpeechResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("en_US"))
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                Locale("Hi from the inside of the android.")
            )
        })
    }

    private fun displayOcr() {
        val chooseIntent = Intent()
        chooseIntent.type = "image/*"
        chooseIntent.action = Intent.ACTION_GET_CONTENT
        startForOcrResult.launch(chooseIntent)
    }

    private val startForSpeechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    .let { text -> text?.get(0) }

            if (spokenText != null && spokenText.isNotEmpty()) {
                goNewContentFragment(spokenText)
            }
        }
    }

    private val startForOcrResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val imageUri = data?.data
        OcrUtils.convertImageToText(
            InputImage.fromFilePath(
                requireContext(),
                imageUri!!
            )
        ) { text ->
            goNewContentFragment(text)
        }
    }

    private fun goNewContentFragment(content: String) {
        val newContentFragment = NewContentFragment.newInstance(content, note)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, newContentFragment)
        transaction.addToBackStack(NewContentFragment::class.java.simpleName)
        transaction.commit()
    }

    private fun speak(text: String) = lifecycleScope.launch {
        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    companion object {
        @JvmStatic
        fun newInstance(note: NoteModel) =
            ViewNoteFragment().apply {
                arguments = Bundle().apply {
                    putString("selectedNote", Json.encodeToString(note))
                }
            }
    }
}