package com.example.clubolimpus

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Female
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Male
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_None
import kotlinx.android.synthetic.main.activity_add_member.*

class AddMemberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        val spinner = spinner

        var gender = 0

        val adapter = ArrayAdapter.createFromResource(this, R.array.spinner_string, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position)) {
                    "Мужской" -> gender == Gender_Male
                    "Женский" -> gender == Gender_Female
                    "Неопределён" -> gender == Gender_None
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
            R.id.save_date -> return true
            R.id.delete_data -> return true
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }
}

