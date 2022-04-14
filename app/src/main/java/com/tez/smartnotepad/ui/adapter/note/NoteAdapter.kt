package com.tez.smartnotepad.ui.adapter.note

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.util.enums.NoteStatus
import com.tez.smartnotepad.util.ext.inflate


class NoteAdapter(private val notes: List<NoteModel>, private inline val onClickListener: NoteModel.() -> Unit): RecyclerView.Adapter<NoteAdapter.BaseHolder>() {

    override fun getItemViewType(position: Int): Int {

        return if (notes[position].contentsContentDtos.isNullOrEmpty())
            NoteStatus.EMPTY.value
        else
            NoteStatus.NONEMPTY.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {

        return if (viewType == NoteStatus.EMPTY.value)
            EmptyNoteHolder(parent.inflate(R.layout.item_note_empty, false))
        else
            NoteHolder(parent.inflate(R.layout.item_note, false))
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bindNote(notes[position], onClickListener)
    }

    abstract class BaseHolder(private val noteItemView: View): RecyclerView.ViewHolder(noteItemView){
        abstract fun bindNote(note: NoteModel, onClickListener: NoteModel.() -> Unit)
    }

    class NoteHolder(private val noteItemView: View): BaseHolder(noteItemView) {

        override fun bindNote(note: NoteModel, onClickListener: NoteModel.() -> Unit, ) {
            val tvTesT = noteItemView.findViewById<TextView>(R.id.tvNoteContent)
            tvTesT.text = note.contentsContentDtos?.first()?.context

            noteItemView.setOnClickListener {
                onClickListener(note)
            }
        }
    }

    class EmptyNoteHolder(private val noteItemView: View): BaseHolder(noteItemView) {
        override fun bindNote(note: NoteModel, onClickListener: NoteModel.() -> Unit) {}
    }

    override fun getItemCount(): Int = notes.size
}