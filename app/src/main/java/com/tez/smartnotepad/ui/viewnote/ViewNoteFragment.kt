package com.tez.smartnotepad.ui.viewnote

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.vm.ViewNoteViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ViewNoteFragment : Fragment() {

    private lateinit var note: NoteModel
    private lateinit var viewNoteViewModel: ViewNoteViewModel
    private lateinit var contentRepository: ContentRepository
    private lateinit var contentRemoteDataSource: ContentRemoteDataSource
    private lateinit var contentService: ContentService
    private lateinit var apiClient: ApiClient
    private lateinit var contentAdapter: ContentAdapter
    private lateinit var contents: MutableList<ContentModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            note = Json.decodeFromString(it!!.getString("selectedNote").toString())
        }

        val user =
            UserModel(userId = "3", mail = "string3", password = "string", nameSurname = "string",null,null)
        apiClient = ApiClient
        contentService = apiClient.getClient().create(ContentService::class.java)
        contentRemoteDataSource = ContentRemoteDataSource(contentService)
        contentRepository = ContentRepository(user, contentRemoteDataSource)
        viewNoteViewModel =
            ViewNoteViewModel(contentRepository) // higher order funcs. buraya taşısam ?
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

        noteTitle.text = note.title
        rvContent.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvContent.setHasFixedSize(true)

        contentAdapter = initAdapter()
        rvContent.adapter = contentAdapter

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
                    viewNoteViewModel.updateContent(changed, {
                        updateContent(position, it)
                    }, { error ->
                        Log.e(name(), error)
                    })
                }
            },
            onDeleteClickListener = { position ->
                viewNoteViewModel.deleteContent(this, {
                    deleteContent(position)
                }, {
                    Log.e(name(), it)
                })
            })
    }

    private fun updateContent(position: Int, updatedContent: ContentModel) {
        viewNoteViewModel.getContentsOfNote(note.noteId, {
            contents[position] = updatedContent
            contentAdapter.notifyItemChanged(position)
        }, {
            Log.e(name(), it)
        })
    }

    private fun deleteContent(position: Int) {
        if (contents.size == 1){
            Toast.makeText(context,"This content was last. Note will be deleted after this content deleted.",Toast.LENGTH_LONG).show()
            viewNoteViewModel.deleteNote(note)
            destroyMe()
        }else{
            viewNoteViewModel.deleteContent(contents[position],{},{})
            contents.removeAt(position)
            contentAdapter.notifyItemRemoved(position)
        }
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
        builder.setNegativeButton("Cancel") {
            dialog, _ -> dialog.cancel()
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
        builder.setNegativeButton("Cancel") {
            dialog, _ -> dialog.cancel()
        }
        builder.show()
    }

    private fun destroyMe(){
        activity?.onBackPressed()
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