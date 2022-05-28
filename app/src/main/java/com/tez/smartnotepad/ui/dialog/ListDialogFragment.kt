package com.tez.smartnotepad.ui.dialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tez.smartnotepad.R
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.ui.adapter.dialog.ParticipantDialogAdapter

class ListDialogFragment(
    private val participantUsers: List<UserModel>,
    private val onItemClick: (userModel: UserModel) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let { it ->

            val view = it.layoutInflater.inflate(R.layout.dialog_recylerview, null)
            val tvParticipants = view.findViewById<RecyclerView>(R.id.dialog_recycler_view)
            val adapter = ParticipantDialogAdapter(participantUsers) {
                onItemClick.invoke(this)
                dialog?.dismiss()
            }
            
            tvParticipants.layoutManager = LinearLayoutManager(activity)
            tvParticipants.adapter = adapter

            val dialog = MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_Material3
            ).setView(view)
                .setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }.create()

            //dialog.window?.setBackgroundDrawableResource(R.color.white_600)
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

