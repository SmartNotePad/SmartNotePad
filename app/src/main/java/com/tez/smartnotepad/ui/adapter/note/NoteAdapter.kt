package com.tez.smartnotepad.ui.adapter.note

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.util.ext.inflate


class NoteAdapter(private val notes: List<NoteModel>): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflatedView = parent.inflate(R.layout.item_note, false)
        return NoteHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = notes[position]
        holder.bindNote(note)
    }

    class NoteHolder(private val noteItemView: View): RecyclerView.ViewHolder(noteItemView) {

        //private var note: NoteModel? = null
        fun bindNote(note: NoteModel) {
            //this.note = note
            val tvTesT = noteItemView.findViewById<TextView>(R.id.tvNoteContent)
            tvTesT.text = note.contentsContentDtos.first().context
        }
    }

    override fun getItemCount(): Int = notes.size
}