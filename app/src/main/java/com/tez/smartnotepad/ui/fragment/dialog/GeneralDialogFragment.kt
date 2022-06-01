package com.tez.smartnotepad.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tez.smartnotepad.R

class GeneralDialogFragment(
    private val oldValue: String?,
    private val onPositiveClick: (value: String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let { it ->

            val view = it.layoutInflater.inflate(R.layout.dialog_general, null)
            val result = view.findViewById<EditText>(R.id.etEditContent)

            oldValue?.let { v ->
                result.setText(v)
            }
            val dialog = MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_Material3
            ).setView(view)
                .setPositiveButton("Confirm") { _, _ ->
                    onPositiveClick.invoke(result.text.toString())
                }.setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }.create()

            dialog.window?.setBackgroundDrawableResource(R.color.white_600)
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

