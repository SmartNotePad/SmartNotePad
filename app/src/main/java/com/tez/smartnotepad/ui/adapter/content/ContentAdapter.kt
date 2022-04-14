package com.tez.smartnotepad.ui.adapter.content

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.util.enums.ContentType
import com.tez.smartnotepad.util.ext.inflate

class ContentAdapter(private var contents: MutableList<ContentModel>, private inline val onEditClickListener: ContentModel.(position: Int) -> Unit, private inline val onDeleteClickListener: ContentModel.(position: Int) -> Unit): RecyclerView.Adapter<ContentAdapter.BaseHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (contents[position].type) {
            ContentType.Text.value -> ContentType.Text.value
            ContentType.Voice.value -> ContentType.Voice.value
            else -> ContentType.Image.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return TextContentHolder(parent.inflate(R.layout.item_content,false))
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bindContent(contents[position], position, onEditClickListener, onDeleteClickListener)
    }

    abstract class BaseHolder(contentItemView: View): RecyclerView.ViewHolder(contentItemView) {
        abstract fun bindContent(content: ContentModel, position: Int, onEditClickListener: ContentModel.(position:Int) -> Unit, onDeleteClickListener: ContentModel.(position: Int) -> Unit)
    }

    class TextContentHolder(private val contentItemView: View): BaseHolder(contentItemView){

        override fun bindContent(content: ContentModel, position: Int,   onEditClickListener: ContentModel.(position:Int) -> Unit, onDeleteClickListener: ContentModel.(position: Int) -> Unit) {

            val contentText = contentItemView.findViewById<TextView>(R.id.tvContent)
            contentText.text = content.context

            val ibEdit = contentItemView.findViewById<ImageButton>(R.id.ibContentEdit)
            val ibDelete = contentItemView.findViewById<ImageButton>(R.id.ibContentDelete)

            ibEdit.setOnClickListener {
                onEditClickListener.invoke(content,position)
            }

            ibDelete.setOnClickListener {
                onDeleteClickListener(content,position)
            }

        }
    }

/*    class VoiceContentHolder(private val contentItemView: View): BaseHolder(contentItemView){
        override fun bindContent(content: ContentModel, onEditClickListener: ContentModel.() -> Unit, onDeleteClickListener: ContentModel.() -> Unit) {
            TODO("Not yet implemented")
        }
    }

    class ImageContentHolder(private val contentItemView: View): BaseHolder(contentItemView){
        override fun bindContent(content: ContentModel, onEditClickListener: ContentModel.() -> Unit, onDeleteClickListener: ContentModel.() -> Unit) {
            TODO("Not yet implemented")
        }
    }*/

    override fun getItemCount(): Int = contents.size

}