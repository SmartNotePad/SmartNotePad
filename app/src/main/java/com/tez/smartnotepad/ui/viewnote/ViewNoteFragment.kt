package com.tez.smartnotepad.ui.viewnote

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import com.tez.smartnotepad.network.service.ContentService
import com.tez.smartnotepad.ui.adapter.content.ContentAdapter
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.vm.ViewNoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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
            note = Json.decodeFromString<NoteModel>(it!!.getString("selectedNote").toString())
        }

        val user =
            UserModel(userId = "3", mail = "string3", password = "string", nameSurname = "string")
        apiClient = ApiClient
        contentService = apiClient.getClient().create(ContentService::class.java)
        contentRemoteDataSource = ContentRemoteDataSource(contentService)
        contentRepository = ContentRepository(user, contentRemoteDataSource)
        viewNoteViewModel = ViewNoteViewModel(contentRepository)
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


        noteTitle.text = note.title
        rvContent.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvContent.setHasFixedSize(true)

        contentAdapter = initAdapter()
        rvContent.adapter = contentAdapter

    }


    private fun initAdapter(): ContentAdapter {
        return ContentAdapter(
            contents,
            onEditClickListener = { position ->
                showEditdialog(this.context, { newValue ->
                    val changed = this.copy(context = newValue)
                    viewNoteViewModel.updateContent(changed, {
                        updateAdapter(position, it)
                    }, { error ->
                        Log.e(name(), error)
                    })
                })
            },
            onDeleteClickListener = { position ->
                viewNoteViewModel.deleteContent(this, {
                    updateAdapter(position,null)
                }, {
                    Log.e(name(), it)
                })
            })
    }


    private fun updateAdapter(position: Int, updatedContent:ContentModel?) {
        viewNoteViewModel.getContentsOfNote(note.noteId, {
            if (updatedContent != null) {
                contents[position] = updatedContent
                contentAdapter.notifyItemChanged(position)
            }else{
                contents.removeAt(position)
                contentAdapter.notifyItemRemoved(position)
            }
        }, {
            Log.e(name(),it)
        })
    }

    private fun showEditdialog(
        oldValue: String,
        getUpdatedContext: (updatedContext: String) -> Unit
    ) {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Edit Content")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(oldValue)
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var m_Text = input.text.toString()
            getUpdatedContext(m_Text)
        })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
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