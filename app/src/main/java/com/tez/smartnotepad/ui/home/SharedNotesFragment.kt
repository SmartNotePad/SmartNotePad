package com.tez.smartnotepad.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.tez.smartnotepad.R
import com.tez.smartnotepad.network.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepositoryImpl
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.ui.newnote.NewNoteFragment
import com.tez.smartnotepad.ui.viewnote.ViewNoteFragment
import com.tez.smartnotepad.util.ocr.OcrUtils
import com.tez.smartnotepad.vm.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SharedNotesFragment : Fragment() {

    private lateinit var user: UserModel
    private lateinit var notes: MutableList<NoteModel>

    val homeViewModel: HomeViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter
    private var textFromOcrOrVoice: String = ""

    private val fabOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_open_anim
        )
    }
    private val fabClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = true

    private lateinit var btnAddNoteNormal: FloatingActionButton
    private lateinit var btnAddNoteWithCamera: FloatingActionButton
    private lateinit var btnAddNoteWithVoice: FloatingActionButton
    private lateinit var btnAddNote: FloatingActionButton

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

        btnAddNote = view.findViewById(R.id.fab_menu_actions)
        btnAddNoteNormal = view.findViewById(R.id.fab_menu_add_normal_note)
        btnAddNoteWithCamera = view.findViewById(R.id.fab_menu_add_camera)
        btnAddNoteWithVoice = view.findViewById(R.id.fab_menu_add_voice)

        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        btnAddNote.setOnClickListener {
            initVisibility(clicked)
            initAnimation(clicked)
            clicked = !clicked
        }

        btnAddNoteNormal.setOnClickListener {
            goNewNoteFragment()
        }

        btnAddNoteWithCamera.setOnClickListener {
            displayOcr()
        }

        btnAddNoteWithVoice.setOnClickListener {
            displaySpeechRecognizer()
        }

        homeViewModel.getMyNotes {
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

    private fun initVisibility(clicked: Boolean) {

        if (clicked) {
            btnAddNoteNormal.visibility = View.VISIBLE
            btnAddNoteWithCamera.visibility = View.VISIBLE
            btnAddNoteWithVoice.visibility = View.VISIBLE
        } else {
            btnAddNoteNormal.visibility = View.GONE
            btnAddNoteWithCamera.visibility = View.GONE
            btnAddNoteWithVoice.visibility = View.GONE
        }
    }

    private fun initAnimation(clicked: Boolean) {
        if (clicked) {
            btnAddNote.startAnimation(fabOpen)

            btnAddNoteNormal.startAnimation(fromBottom)
            btnAddNoteWithCamera.startAnimation(fromBottom)
            btnAddNoteWithVoice.startAnimation(fromBottom)
        } else {
            btnAddNote.startAnimation(fabClose)

            btnAddNoteNormal.startAnimation(toBottom)
            btnAddNoteWithCamera.startAnimation(toBottom)
            btnAddNoteWithVoice.startAnimation(toBottom)
        }
    }

    private fun initAdapter(notes: List<NoteModel>): NoteAdapter {
        return NoteAdapter(notes,
            onNoteClickListener = {
                goViewNoteFragment(this)
            }, onDeleteClickListener = { position ->
                deleteNote(position, this)
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

    private fun displaySpeechRecognizer() {
        startForSpeechResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("en_US"))
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                Locale("Hi from the inside of the android on windows. Sanki inception.")
            )
        })
    }

    private fun displayOcr() {
        val chooseIntent = Intent()
        chooseIntent.type = "image/*"
        chooseIntent.action = Intent.ACTION_GET_CONTENT
        startForOcrResult.launch(chooseIntent)
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