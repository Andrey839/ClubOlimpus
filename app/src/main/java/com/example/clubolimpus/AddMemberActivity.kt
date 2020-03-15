package com.example.clubolimpus

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.clubolimpus.data.ClubOlimpusContract
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Female
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_Male
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry.Companion.Gender_None
import kotlinx.android.synthetic.main.activity_add_member.*

class AddMemberActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        const val editMemberLoader = 111
    }

    var uriMember: Uri? = null
    var gender = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        val intent = intent
        uriMember = intent.data

        if (uriMember == null) {
            title = "Добавить нового члена клуба"
            invalidateOptionsMenu()
        } else {
            title = "Изменить члена клуба"
            supportLoaderManager.initLoader(editMemberLoader, null, this)
        }

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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        val menuItem = menu?.findItem(R.id.delete_data)
        menuItem?.isVisible = false
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        MenuInflater(this).inflate(R.menu.save_and_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save_date -> saveMember()
            R.id.delete_data -> showDeleteDialog()
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Вы хотите удалить пользователя?")
        dialog.setPositiveButton("Удалить") { dialog, which ->
            if (uriMember != null) {
                val rowsDelete = contentResolver.delete(uriMember!!, null, null)
                if (rowsDelete == 0) Toast.makeText(this, "Удаление не удалось", Toast.LENGTH_LONG)
                    .show()
                else Toast.makeText(this, "Пользователь удалён", Toast.LENGTH_LONG).show()
            }
        }
        dialog.setNegativeButton("Отмена") { dialog, which -> dialog?.dismiss() }
        dialog.create().show()
    }

    private fun saveMember() {
        val name: String = editTextName.text.trim().toString()
        val surname: String = editTextSurname.text.trim().toString()
        val sport: String = editTextGroup.text.trim().toString()

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
            return
        } else {
            if (TextUtils.isEmpty(surname)) {
                Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show()
                return
            } else {
                if (TextUtils.isEmpty(sport)) {
                    Toast.makeText(this, "Введите вид спорта", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        val contentValues = ContentValues()
        contentValues.apply {
            put(ClubOlimpusContract.MemberEntry.KEY_NAME, name)
            put(ClubOlimpusContract.MemberEntry.KEY_SURNAME, surname)
            put(ClubOlimpusContract.MemberEntry.KEY_SPORT, sport)
            put(ClubOlimpusContract.MemberEntry.KEY_GENDER, gender)
        }

        if (uriMember == null) {
            val uri =
                contentResolver.insert(ClubOlimpusContract.MemberEntry.contentUri, contentValues)

            if (uri == null) Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_LONG).show()
            else Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show()
        } else {
            val rowsChanger = contentResolver.update(uriMember!!, contentValues, null, null)
            if (rowsChanger == 0) {
                Toast.makeText(this, "Редактирование не удалось", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Редактирование успешное", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            ClubOlimpusContract.MemberEntry.KEY_ID,
            ClubOlimpusContract.MemberEntry.KEY_NAME,
            ClubOlimpusContract.MemberEntry.KEY_SURNAME,
            ClubOlimpusContract.MemberEntry.KEY_GENDER,
            ClubOlimpusContract.MemberEntry.KEY_SPORT
        )

        return CursorLoader(this, uriMember!!, projection, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data!!.moveToFirst()) {
            val name = Editable.Factory.getInstance()
                .newEditable(data.getString(data.getColumnIndexOrThrow(ClubOlimpusContract.MemberEntry.KEY_NAME)))
            val surName = Editable.Factory.getInstance()
                .newEditable(data.getString(data.getColumnIndexOrThrow(ClubOlimpusContract.MemberEntry.KEY_SURNAME)))
            val gender =
                data.getInt(data.getColumnIndexOrThrow(ClubOlimpusContract.MemberEntry.KEY_GENDER))
            val sport = Editable.Factory.getInstance()
                .newEditable(data.getString(data.getColumnIndexOrThrow(ClubOlimpusContract.MemberEntry.KEY_SPORT)))

            editTextName.text = name
            editTextSurname.text = surName
            editTextGroup.text = sport

            when (gender) {
                Gender_Male -> spinner.setSelection(1)
                Gender_Female -> spinner.setSelection(2)
                Gender_None -> spinner.setSelection(0)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }

}

