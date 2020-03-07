package com.example.clubolimpus

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.clubolimpus.data.ClubOlimpusContract
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Female
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Male
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_None
import kotlinx.android.synthetic.main.activity_add_member.*
import kotlin.properties.Delegates

class AddMemberActivity : AppCompatActivity() {

    var gender = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        val spinner = spinner

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_string,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                gender = Gender_None
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position)) {
                    "Мужской" -> gender = Gender_Male
                    "Женский" -> gender = Gender_Female
                    "Неопределён" -> gender = Gender_None
                }

            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        MenuInflater(this).inflate(R.menu.save_and_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_date -> insertMember()
            R.id.delete_data -> return true
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertMember(name: String = editTextName.text.trim().toString(), surname: String = editTextSurname.text.trim().toString(), sport: String = editTextGroup.text.trim().toString()) {
        val contentValues = ContentValues()
        contentValues.apply {
            put(ClubOlimpusContract.MemberEntry.KEY_NAME, name)
            put(ClubOlimpusContract.MemberEntry.KEY_SURNAME, surname)
            put(ClubOlimpusContract.MemberEntry.KEY_SPORT, sport)
            put(ClubOlimpusContract.MemberEntry.KEY_GENDER, gender)
        }

        val contentResolver = contentResolver
        val uri = contentResolver.insert(ClubOlimpusContract.MemberEntry.contentUri, contentValues)

        if (uri == null) Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show()

    }
}

