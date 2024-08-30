package com.codeseyee.cloydcontact.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import com.codeseyee.cloydcontact.EditIndividualProfileActvity
import com.codeseyee.cloydcontact.R

object AppUtils {

    fun showProgressDialog(context: Context, title: String, message: String): Dialog {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog, null)
        view.findViewById<TextView>(R.id.progress_title).text = title
        view.findViewById<TextView>(R.id.progress_message).text = message
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }

    fun showYesNoDialog(context: Context, title: String, message: String, listener: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            listener()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun launchProfileEditActivity(context: Context) {
        val intent = Intent(context, EditIndividualProfileActvity::class.java)
        context.startActivity(intent)
    }
}
