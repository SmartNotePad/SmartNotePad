package com.tez.smartnotepad.ui.viewnote

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.common.InputImage
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.service.ContentService
import com.tez.smartnotepad.ui.adapter.content.ContentAdapter
import com.tez.smartnotepad.ui.content.NewContentFragment
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.util.ocr.OcrUtils
import com.tez.smartnotepad.vm.ViewNoteViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ViewNoteFragment : Fragment() {

    private lateinit var note: NoteModel
    private lateinit var viewNoteViewModel: ViewNoteViewModel
    private lateinit var contentRepository: ContentRepository
    private lateinit var contentRemoteDataSource: ContentRemoteDataSource
    private lateinit var contentService: ContentService
    private lateinit var apiClient: ApiClient
    private lateinit var contentAdapter: ContentAdapter
    private lateinit var contents: MutableList<ContentModel>
    private var textFromOcrOrVoice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = Json.decodeFromString(it.getString("selectedNote").toString())
        }

        val user =
            UserModel(
                userId = "3",
                mail = "string2",
                password = "string",
                nameSurname = "string",
                null,
                null
            )
        apiClient = ApiClient
        contentService = apiClient.getClient().create(ContentService::class.java)
        contentRemoteDataSource = ContentRemoteDataSource(contentService)
        contentRepository = ContentRepository(user, contentRemoteDataSource)
        viewNoteViewModel =
            ViewNoteViewModel(contentRepository,startForSpeechResult, startForOcrResult) // higher order funcs. buraya taşısam ?
        contents = note.contentsContentDtos!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteTitle = view.findViewById<TextView>(R.id.tvNoteTitle)
        val rvContent = view.findViewById<RecyclerView>(R.id.rvNoteContents)
        val btnShareNote = view.findViewById<Button>(R.id.btnShareNote)
        val btnAddContentNormal = view.findViewById<Button>(R.id.btnAddContentText)
        val btnAddContentWithCamera = view.findViewById<Button>(R.id.btnAddContentCamera)
        val btnAddContentWithSpeech = view.findViewById<Button>(R.id.btnAddContentVoice)

        noteTitle.text = note.title

        rvContent.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvContent.setHasFixedSize(true)

        contentAdapter = initAdapter()
        rvContent.adapter = contentAdapter

        btnAddContentNormal.setOnClickListener {
            textFromOcrOrVoice = ""
            goNewContentFragment()
        }

        btnAddContentWithSpeech.setOnClickListener {
            viewNoteViewModel.displaySpeechRecognizer()
        }

        btnAddContentWithCamera.setOnClickListener {
            viewNoteViewModel.displayOcr()
        }



        btnShareNote.setOnClickListener {
            showShareDialog(
                getSharedUserMail = { mail ->
                    viewNoteViewModel.shareNote(ShareNoteModel(note.userUserId, note.noteId, mail),
                        {
                            Log.e(name(), it.toString())
                        }, {
                            Log.e(name(), it)
                        })
                })
        }

    }

    private fun initAdapter(): ContentAdapter {
        return ContentAdapter(
            contents,
            onEditClickListener = { position ->
                showEditDialog(this.context) { newValue ->
                    val changed = this.copy(context = newValue)
                    updateContent(position, changed)
                }
            },
            onDeleteClickListener = { position ->
                deleteContent(position, this)
            })
    }

    private fun updateContent(position: Int, changedContent: ContentModel) {
        viewNoteViewModel.updateContent(changedContent, {
            contents[position] = it
            contentAdapter.notifyItemChanged(position)
        }, { error ->
            Log.e(name(), error)
        })
    }

    private fun deleteContent(position: Int, content: ContentModel) {

        viewNoteViewModel.deleteContent(content, {
            contents.removeAt(position)
            contentAdapter.notifyItemRemoved(position)
            Log.e("Silindi. Run Delete ($position)", content.context)
        }, {
            Log.e("Content silinirken Hata", it)
        })
    }

    private fun showEditDialog(
        oldValue: String,
        getUpdatedContext: (updatedContext: String) -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Content")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(oldValue)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            getUpdatedContext(input.text.toString())
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showShareDialog(
        getSharedUserMail: (mail: String) -> Unit
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Enter User Mail")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            getSharedUserMail(input.text.toString())
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }


    private val startForSpeechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    .let { text -> text?.get(0) }

            if (spokenText != null && spokenText.isNotEmpty()) {
                textFromOcrOrVoice = spokenText
                goNewContentFragment()
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
            textFromOcrOrVoice = text
            goNewContentFragment()
        }
    }

/*    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(activity) {
            if (it == TextToSpeech.SUCCESS) textToSpeechEngine.language = Locale("en_US")
        }
    }

    fun speak(text: String) = lifecycleScope.launch{
        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }*/

    private fun goNewContentFragment() {
        val newContentFragment = NewContentFragment.newInstance(textFromOcrOrVoice, note)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, newContentFragment)
        transaction.addToBackStack(NewContentFragment::class.java.simpleName)
        transaction.commit()
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