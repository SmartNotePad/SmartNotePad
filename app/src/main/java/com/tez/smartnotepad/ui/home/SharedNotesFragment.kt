package com.tez.smartnotepad.ui.home

import android.app.Activity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.ui.newnote.NewNoteFragment
import com.tez.smartnotepad.ui.viewnote.ViewNoteFragment
import com.tez.smartnotepad.util.ocr.OcrUtils
import com.tez.smartnotepad.vm.HomeViewModel

class SharedNotesFragment : Fragment() {

    private lateinit var user: UserModel

    private lateinit var notes: MutableList<NoteModel>

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var noteRepository: NoteRepository
    private lateinit var noteRemoteDataSource: NoteRemoteDataSource
    private lateinit var noteService: NoteService
    private lateinit var apiClient: ApiClient
    private lateinit var noteAdapter: NoteAdapter
    private var textFromOcrOrVoice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        noteRemoteDataSource = NoteRemoteDataSource(noteService)
        noteRepository = NoteRepository(user, noteRemoteDataSource)
        homeViewModel = HomeViewModel(noteRepository, startForSpeechResult, startForOcrResult)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recy)
        val tvZeroNoteInfo = view.findViewById<TextView>(R.id.tvZeroNoteInfo)

        val btnAddNoteNormal = view.findViewById<FloatingActionButton>(R.id.fab_menu_add_normal_note)
        val btnAddNoteWithCamera = view.findViewById<FloatingActionButton>(R.id.fab_menu_add_camera)
        val btnAddNoteWithVoice = view.findViewById<FloatingActionButton>(R.id.fab_menu_add_voice)

        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        btnAddNoteNormal.setOnClickListener {
            goNewNoteFragment()
        }

        btnAddNoteWithCamera.setOnClickListener {
            homeViewModel.displayOcr()
        }

        btnAddNoteWithVoice.setOnClickListener {
            homeViewModel.displaySpeechRecognizer()
        }

        homeViewModel.getSharedNotesWithMe {
            notes = it
            if (notes.isEmpty()) {
                tvZeroNoteInfo.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            } else {
                noteAdapter = initAdapter(notes)
                recyclerView.adapter = noteAdapter
            }
        }
    }

    private fun initAdapter(notes: List<NoteModel>): NoteAdapter {
        return NoteAdapter(notes,
            onNoteClickListener = {
                goViewNoteFragment(this)
            }, onDeleteClickListener = { position ->
                deleteNote(position,this)
            }
        )
    }

    private fun deleteNote(position: Int, note: NoteModel) {

        homeViewModel.deleteNote(note, {
            notes.removeAt(position)
            noteAdapter.notifyItemRemoved(position)
            Log.e("Silindi. Run Delete ($position)", note.title)
        }, {
            Log.e("Content silinirken Hata", "aasd")
        })
    }

    private val startForSpeechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    .let { text -> text?.get(0) }

            if (spokenText != null && spokenText.isNotEmpty()) {
                textFromOcrOrVoice = spokenText
                goNewNoteFragment()
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
            goNewNoteFragment()
        }
    }

    private fun goViewNoteFragment(note: NoteModel) {
        val viewNoteFragment = ViewNoteFragment.newInstance(note)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, viewNoteFragment)
        transaction.addToBackStack(ViewNoteFragment::class.java.simpleName)
        transaction.commit()
    }

    private fun goNewNoteFragment() {
        val newNoteFragment = NewNoteFragment.newInstance(textFromOcrOrVoice)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, newNoteFragment)
        transaction.addToBackStack(NewNoteFragment::class.java.simpleName)
        transaction.commit()
    }
}