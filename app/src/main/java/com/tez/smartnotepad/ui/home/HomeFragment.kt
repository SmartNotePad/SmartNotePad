package com.tez.smartnotepad.ui.home

import android.os.Bundle
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
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
import com.tez.smartnotepad.ui.newnote.NewNoteFragment
import com.tez.smartnotepad.ui.viewnote.ViewNoteFragment
import com.tez.smartnotepad.vm.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var user:UserModel

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var noteRepository: NoteRepository
    private lateinit var noteRemoteDataSource: NoteRemoteDataSource
    private lateinit var noteService: NoteService
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = UserModel(userId="3", mail="string3", password="string", nameSurname="string")
        apiClient = ApiClient
        noteService = apiClient.getClient().create(NoteService::class.java)
        noteRemoteDataSource = NoteRemoteDataSource(noteService)
        noteRepository = NoteRepository(user,noteRemoteDataSource)
        homeViewModel = HomeViewModel(noteRepository)

        homeViewModel.getAllNotesOfUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_note, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recy)
        val tvZeroNoteInfo = view.findViewById<TextView>(R.id.tvZeroNoteInfo)
        val btnAddNoteNormal = view.findViewById<FloatingActionButton>(R.id.fab_menu_add_normal_note)

        recyclerView.layoutManager = GridLayoutManager(context,2,RecyclerView.VERTICAL,false)
        btnAddNoteNormal.setOnClickListener {
            goNewNoteFragment()
        }

        homeViewModel.notes.observe(viewLifecycleOwner){
            if(it.isEmpty()) {
                tvZeroNoteInfo.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }else{
                recyclerView.adapter = NoteAdapter(it) {
                    goViewNoteFragment(this)
                }
            }
        }
        return view
    }

    private fun goViewNoteFragment(note: NoteModel) {
        val viewNoteFragment = ViewNoteFragment.newInstance(note)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, viewNoteFragment)
        transaction.addToBackStack(NewNoteFragment::class.java.simpleName)
        transaction.commit()
    }

    private fun goNewNoteFragment() {
        val newNoteFragment = NewNoteFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, newNoteFragment)
        transaction.addToBackStack(NewNoteFragment::class.java.simpleName)
        transaction.commit()
    }
}