package com.tez.smartnotepad.ui.fragment.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.common.InputImage
import com.tez.smartnotepad.R
import com.tez.smartnotepad.core.BaseFragmentWithViewModel
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.databinding.FragmentHomeNotesBinding
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.ui.fragment.newnote.NewNoteFragment
import com.tez.smartnotepad.ui.fragment.viewnote.ViewNoteFragment
import com.tez.smartnotepad.util.ocr.OcrUtils
import com.tez.smartnotepad.vm.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MyNotesFragment :
    BaseFragmentWithViewModel<FragmentHomeNotesBinding, HomeViewModel>(
        FragmentHomeNotesBinding::inflate
    ) {

    @Inject
    lateinit var sharedPreferences: PrefDataSource
    private lateinit var user: UserModel

    override val viewModel: HomeViewModel by viewModels()
    private lateinit var notes: MutableList<NoteModel>
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences.user?.let {
            this.user = it
        }
    }

    override fun initContentsOfViews() {
        with(binding) {
            recy.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            viewModel.getMyNotes {
                notes = it
                if (notes.isEmpty()) {
                    tvZeroNoteInfo.visibility = View.VISIBLE
                    recy.visibility = View.INVISIBLE
                } else {
                    noteAdapter = initMyNotesAdapter(notes)
                    recy.adapter = noteAdapter
                }
            }
        }
    }

    override fun initListener() {
        with(binding) {
            fabMenuActions.setOnClickListener {
                initVisibility(clicked)
                initAnimation(clicked)
                clicked = !clicked
            }

            fabMenuAddNormalNote.setOnClickListener {
                goNewNoteFragment()
            }

            fabMenuAddCamera.setOnClickListener {
                displayOcr()
            }

            fabMenuAddVoice.setOnClickListener {
                displaySpeechRecognizer()
            }
        }
    }

    private fun initVisibility(clicked: Boolean) {
        with(binding) {
            if (clicked) {
                fabMenuAddNormalNote.visibility = View.VISIBLE
                fabMenuAddCamera.visibility = View.VISIBLE
                fabMenuAddVoice.visibility = View.VISIBLE
            } else {
                fabMenuAddNormalNote.visibility = View.GONE
                fabMenuAddCamera.visibility = View.GONE
                fabMenuAddVoice.visibility = View.GONE
            }
        }
    }

    private fun initAnimation(clicked: Boolean) {
        with(binding) {
            if (clicked) {
                fabMenuActions.startAnimation(fabOpen)
                fabMenuAddNormalNote.startAnimation(fromBottom)
                fabMenuAddCamera.startAnimation(fromBottom)
                fabMenuAddVoice.startAnimation(fromBottom)
            } else {
                fabMenuActions.startAnimation(fabClose)
                fabMenuAddNormalNote.startAnimation(toBottom)
                fabMenuAddCamera.startAnimation(toBottom)
                fabMenuAddVoice.startAnimation(toBottom)
            }
        }
    }

    private fun initMyNotesAdapter(notes: List<NoteModel>): NoteAdapter {
        return NoteAdapter(notes,
            onNoteClickListener = {
                goViewNoteFragment(this)
            }, onDeleteClickListener = { position ->
                deleteNote(position, this)
            }
        )
    }

    private fun deleteNote(position: Int, note: NoteModel) {

        viewModel.deleteNote(note,
            {
                notes.removeAt(position)
                noteAdapter.notifyItemRemoved(position)
                showMessage("Not silindi.")
            }, {
                showMessage(it)
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