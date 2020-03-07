package com.example.clubolimpus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton = floatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        displayData()
    }

    private fun displayData() {

        val projection = arrayOf(
            MemberEntry.KEY_ID,
            MemberEntry.KEY_NAME,
            MemberEntry.KEY_SURNAME,
            MemberEntry.KEY_GENDER,
            MemberEntry.KEY_SPORT
        )

        val cursor = contentResolver.query(MemberEntry.contentUri, projection, null, null, null)

        textView.text = "All users\n\n"
        textView.append(
            "$MemberEntry.KEY_ID" + " " +
                    "$MemberEntry.KEY_NAME" + " " +
                    "$MemberEntry.KEY_SURNAME" + " " +
                    "$MemberEntry.KEY_GENDER" + " " +
                    "$MemberEntry.KEY_SPORT\n"
        )

        val indexId = cursor?.getColumnIndex(MemberEntry.KEY_ID)
        val indexName = cursor?.getColumnIndex(MemberEntry.KEY_NAME)
        val indexSurname = cursor?.getColumnIndex(MemberEntry.KEY_SURNAME)
        val indexGender = cursor?.getColumnIndex(MemberEntry.KEY_GENDER)
        val indexSport = cursor?.getColumnIndex(MemberEntry.KEY_SPORT)

        while (cursor!!.moveToNext()) {
            val currentId = cursor.getInt(indexId!!)
            val currentName = cursor.getString(indexName!!)
            val currentSurname = cursor.getString(indexSurname!!)
            val currentGender = cursor.getInt(indexGender!!)
            val currentSport = cursor.getString(indexSport!!)

            textView.append(
                "\n $currentId" + " " +
                        "$currentName" + " " + "$currentSurname" + " " + "$currentGender" + " " + "$currentSport"
            )
        }
    }
}
