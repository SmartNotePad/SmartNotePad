package com.tez.smartnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.data.model.NoteModel

class NoteAdapter(private val notes: MutableList<NoteModel>)
    : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyler_item,parent,false)

        // return the view holder
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textviewnote.text = notes[position].note_content
        holder.textviewlastdate.text = notes[position].note_last_date
    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return notes.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textviewnote: TextView = itemView.findViewById(R.id.textViewNoteContent)
        val textviewlastdate: TextView = itemView.findViewById(R.id.textViewLastDate)
    }


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}