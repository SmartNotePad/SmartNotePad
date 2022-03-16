package com.tez.smartnotepad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainNoteFragment : Fragment() {

   private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_main_note, container, false)
        recyclerView=view.findViewById(R.id.recy)
        recyclerView.layoutManager=GridLayoutManager(context,2,RecyclerView.VERTICAL,false)
        val notes = mutableListOf(
           NoteDAO(1,"asd","qwdas","asd"),
            NoteDAO(2,"asd","qwdas","asd"),
                    NoteDAO(1,"asd","qwdas","asd"),
        NoteDAO(2,"asd","qwdas","asd"),
        NoteDAO(1,"asd","qwdas","asd"),
            NoteDAO(1,"asd","qwdas","asd"),
            NoteDAO(2,"asd","qwdas","asd"),
            NoteDAO(1,"asd","qwdas","asd"),
            NoteDAO(2,"asd","qwdas","asd"),
            NoteDAO(1,"asd","qwdas","asd"),
        NoteDAO(2,"asd","qwdas","asd")

        )
        recyclerView.adapter=NoteAdapter(notes)




        return view
    }



}