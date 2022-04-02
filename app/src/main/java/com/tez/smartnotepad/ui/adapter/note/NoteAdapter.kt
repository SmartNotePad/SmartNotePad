package com.tez.smartnotepad.ui.adapter.note

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.util.ext.NoteStatus
import com.tez.smartnotepad.util.ext.inflate


class NoteAdapter(private val notes: List<NoteModel>): RecyclerView.Adapter<NoteAdapter.BaseHolder>() {

    override fun getItemViewType(position: Int): Int {

        return if (notes.get(position).contentsContentDtos.isEmpty())
            NoteStatus.EMPTY.statusValue
        else
            NoteStatus.NONEMPTY.statusValue
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        if (viewType.equals(NoteStatus.EMPTY.statusValue)){
            val inflatedView = parent.inflate(R.layout.item_note_empty, false)
            return EmptyNoteHolder(inflatedView)
        }else{
            val inflatedView = parent.inflate(R.layout.item_note, false)
            return NoteHolder(inflatedView)
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val note = notes[position]
        holder.bindNote(note)
    }

    abstract class BaseHolder(private val noteItemView: View): RecyclerView.ViewHolder(noteItemView){
        abstract fun bindNote(note: NoteModel)
    }

    class NoteHolder(private val noteItemView: View): BaseHolder(noteItemView) {
        override fun bindNote(note: NoteModel) {
            val tvTesT = noteItemView.findViewById<TextView>(R.id.tvNoteContent)
            tvTesT.text = note.contentsContentDtos.first().context
        }
    }

    class EmptyNoteHolder(private val noteItemView: View): BaseHolder(noteItemView) {
        override fun bindNote(note: NoteModel) {}
    }

    override fun getItemCount(): Int = notes.size
}