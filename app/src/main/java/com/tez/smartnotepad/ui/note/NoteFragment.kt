package com.tez.smartnotepad.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.util.ext.name
import com.tez.smartnotepad.vm.NoteViewModel


class NoteFragment : Fragment() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var user:UserModel

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteRepository: NoteRepository
    private lateinit var noteRemoteDataSource: NoteRemoteDataSource
    private lateinit var noteService: NoteService
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = UserModel(userId="3", mail="string3", password="string3", nameSurname="string3")
        apiClient = ApiClient
        noteService = apiClient.getClient().create(NoteService::class.java)
        noteRemoteDataSource = NoteRemoteDataSource(noteService)
        noteRepository = NoteRepository(user,noteRemoteDataSource)
        noteViewModel = NoteViewModel(noteRepository)

        noteViewModel.getAllNotesOfUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recy)
        val tvZeroNoteInfo = view.findViewById<TextView>(R.id.tvZeroNoteInfo)
        val btn_addNote_normal = view.findViewById<FloatingActionButton>(R.id.fab_menu_add_normal_note)

        recyclerView.layoutManager = GridLayoutManager(context,2,RecyclerView.VERTICAL,false)
        btn_addNote_normal.setOnClickListener {
            goNewNoteFragment()
        }

        noteViewModel.notes.observe(viewLifecycleOwner){
            if(it.isEmpty()) {
                tvZeroNoteInfo.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }else{
                Log.e(name(),it.toString())
                recyclerView.adapter = NoteAdapter(it)
                /*
                    tvZeroNoteInfo.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                */
            }
        }
        return view
    }

    private fun goNewNoteFragment() {
        val newNoteFragment = NewNoteFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, newNoteFragment)
        transaction.addToBackStack(NewNoteFragment::class.java.simpleName)
        transaction.commit()
    }
}