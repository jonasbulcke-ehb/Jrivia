package be.ehb.gdt.jrivia.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import be.ehb.gdt.jrivia.R

class ConfirmScoreDeletionDialogFragment(private val listener: ConfirmScoreDeletionDialogListener) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialogTheme)

            builder.setTitle(getString(R.string.delete_selected_score))
                .setMessage(R.string.score_deletion_warning)
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
                .setPositiveButton(R.string.delete) { _, _ ->
                    listener.onDialogPositiveClick(this)
                }

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDialogDismiss()
    }

    interface ConfirmScoreDeletionDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogDismiss()
    }
}