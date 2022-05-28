package com.tez.smartnotepad.ui.adapter.dialog

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.util.ext.inflate

class ParticipantDialogAdapter(
    private val participantUsers: List<UserModel>,
    private val onClickListener: UserModel.() -> Unit
) :
    RecyclerView.Adapter<ParticipantDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_dialog_user, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(participantUsers[position], onClickListener)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(user: UserModel, onClickListener: UserModel.() -> Unit) {

            val tvUserMail: TextView = itemView.findViewById(R.id.tvUserMail)

            tvUserMail.text = user.mail
            tvUserMail.setOnClickListener {
                onClickListener.invoke(user)
            }
        }
    }

    override fun getItemCount(): Int = participantUsers.size
}