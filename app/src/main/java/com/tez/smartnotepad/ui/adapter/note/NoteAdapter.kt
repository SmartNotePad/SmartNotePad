package com.tez.smartnotepad.ui.adapter.note

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.util.enums.NoteStatus
import com.tez.smartnotepad.util.ext.getParsedDate
import com.tez.smartnotepad.util.ext.inflate


class NoteAdapter(
    private val notes: List<NoteModel>,
    private inline val onNoteClickListener: NoteModel.() -> Unit,
    private inline val onDeleteClickListener: NoteModel.(position: Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.BaseHolder>() {

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
        holder.bindNote(notes[position], onNoteClickListener, onDeleteClickListener)
    }

    abstract class BaseHolder(noteItemView: View) : RecyclerView.ViewHolder(noteItemView) {
        abstract fun bindNote(
            note: NoteModel,
            onNoteClickListener: NoteModel.() -> Unit,
            onDeleteClickListener: NoteModel.(position: Int) -> Unit
        )
    }

    class NoteHolder(private val noteItemView: View) : BaseHolder(noteItemView) {

        override fun bindNote(
            note: NoteModel,
            onNoteClickListener: NoteModel.() -> Unit,
            onDeleteClickListener: NoteModel.(position: Int) -> Unit
        ) {
            val tvTitle = noteItemView.findViewById<TextView>(R.id.tvNoteItemTitle)
            val tvDate = noteItemView.findViewById<TextView>(R.id.tvNoteItemCreatedDate)
            val tvContent = noteItemView.findViewById<TextView>(R.id.tvNoteItemContent)
            val ibtnDelte = noteItemView.findViewById<ImageButton>(R.id.btnNoteItemDeleteNote)

            tvTitle.text = note.title
            tvDate.text = note.createdDate?.getParsedDate()
            tvContent.text = note.contentsContentDtos?.first()?.context

            ibtnDelte.setOnClickListener {
                onDeleteClickListener(note, adapterPosition)
            }

            noteItemView.setOnClickListener {
                onNoteClickListener(note)
            }
        }
    }

    class EmptyNoteHolder(noteItemView: View) : BaseHolder(noteItemView) {
        override fun bindNote(
            note: NoteModel,
            onNoteClickListener: NoteModel.() -> Unit,
            onDeleteClickListener: NoteModel.(position: Int) -> Unit
        ) {
        }
    }

    override fun getItemCount(): Int = notes.size
}