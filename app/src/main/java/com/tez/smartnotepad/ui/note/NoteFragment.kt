package com.tez.smartnotepad.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.datasource.api.ApiClient
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.service.NoteService
import com.tez.smartnotepad.ui.adapter.note.NoteAdapter
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
        user = UserModel(userId="2", mail="string", password="string", nameSurname="tring")
        apiClient = ApiClient
        noteService = apiClient.getClient().create(NoteService::class.java)
        noteRemoteDataSource = NoteRemoteDataSource(noteService)
        noteRepository = NoteRepository(user,noteRemoteDataSource)
        noteViewModel = NoteViewModel(noteRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        noteViewModel.getAllNotesOfUser()
        val view = inflater.inflate(R.layout.fragment_main_note, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recy)
        recyclerView.layoutManager = GridLayoutManager(context,2,RecyclerView.VERTICAL,false)


        noteViewModel.notes.observe(viewLifecycleOwner){
            recyclerView.adapter = NoteAdapter(it)
            Log.e(NoteFragment::class.java.simpleName,it.toString())
        }

        return view
    }
}