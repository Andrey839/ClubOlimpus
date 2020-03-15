package com.example.clubolimpus.data

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import com.example.clubolimpus.R
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry

class MemberCursorAdapter(context: Context?, c: Cursor?, autoRequery: Boolean) :
    CursorAdapter(context, c, autoRequery) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.member_item, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {

        val textName = view!!.findViewById<TextView>(R.id.textMemberName)
        val textSurname = view.findViewById<TextView>(R.id.textMemberSurname)
        val textSport = view.findViewById<TextView>(R.id.textMemberSport)

        val name = cursor?.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_NAME))
        val surname = cursor?.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SURNAME))
        val sport = cursor?.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SPORT))

        textName.text = name
        textSurname.text = surname
        textSport.text = sport


    }
}