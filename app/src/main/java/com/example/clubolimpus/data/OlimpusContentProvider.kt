package com.example.clubolimpus.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.widget.Toast

class OlimpusContentProvider : ContentProvider() {

    lateinit var dbOpenHelper: OlimpusDbOpenHelper
    lateinit var cursor: Cursor

    companion object {
        const val members = 11
        const val memberId = 22
    }

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sURIMatcher.addURI(ClubOlimpusContract.authority, ClubOlimpusContract.pathMembers, members)
        sURIMatcher.addURI(
            ClubOlimpusContract.authority,
            ClubOlimpusContract.pathMembers + "/#",
            memberId
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbOpenHelper.writableDatabase

        if (values?.getAsString(ClubOlimpusContract.MemberEntry.KEY_NAME) == null) Toast.makeText(
            context,
            "Введите Имя",
            Toast.LENGTH_SHORT
        ).show()
        if (values?.getAsString(ClubOlimpusContract.MemberEntry.KEY_SURNAME) == null) Toast.makeText(
            context,
            "Введите Фамлию",
            Toast.LENGTH_SHORT
        ).show()
        if (values?.getAsString(ClubOlimpusContract.MemberEntry.KEY_SPORT) == null) Toast.makeText(
            context,
            "Введите вид Спорта",
            Toast.LENGTH_SHORT
        ).show()

        when (sURIMatcher.match(uri)) {
            members -> {
                val id = db.insert(ClubOlimpusContract.MemberEntry.TABLE_NAME, null, values)
                if (id.toInt() == -1) {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
                    return null
                }
                context?.contentResolver?.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            else -> throw IllegalArgumentException("Ошибка")
        }

    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db: SQLiteDatabase = dbOpenHelper.readableDatabase

        when (sURIMatcher.match(uri)) {
            members -> cursor = db.query(
                ClubOlimpusContract.MemberEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            memberId -> cursor = db.query(
                ClubOlimpusContract.MemberEntry.TABLE_NAME,
                projection,
                ClubOlimpusContract.MemberEntry.KEY_ID + "=?",
                arrayOf(arrayOf(ContentUris.parseId(uri)).toString()),
                null,
                null,
                sortOrder
            )
            else -> Toast.makeText(context, "неправильное значение URI", Toast.LENGTH_LONG).show()
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor

    }

    override fun onCreate(): Boolean {
        dbOpenHelper = OlimpusDbOpenHelper(context)
        return true

    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        val db = dbOpenHelper.writableDatabase

        if (values!!.containsKey(ClubOlimpusContract.MemberEntry.KEY_NAME)) {
            if (values.getAsString(ClubOlimpusContract.MemberEntry.KEY_NAME) == null) Toast.makeText(
                context,
                "Введите имя",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (values.containsKey(ClubOlimpusContract.MemberEntry.KEY_SURNAME)) {
            if (values.getAsString(ClubOlimpusContract.MemberEntry.KEY_SURNAME) == null) Toast.makeText(
                context,
                "Введите Фамлию",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (values.containsKey(ClubOlimpusContract.MemberEntry.KEY_SPORT)) {
            if (values.getAsString(ClubOlimpusContract.MemberEntry.KEY_SPORT) == null) Toast.makeText(
                context,
                "Введите вид спорта",
                Toast.LENGTH_SHORT
            ).show()
        }
        var rowsUpdate: Int

        when (sURIMatcher.match(uri)) {
            members -> {
                rowsUpdate = db.update(
                    ClubOlimpusContract.MemberEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                )

            }
            memberId -> {
                val selection = ClubOlimpusContract.MemberEntry.KEY_ID + "=?"
                val selectionArgs = arrayOf(arrayOf(ContentUris.parseId(uri)).toString())
                rowsUpdate = db.update(
                    ClubOlimpusContract.MemberEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                )

            }
            else -> throw IllegalArgumentException("Не верный URI query")
        }
        if (rowsUpdate != 0) context?.contentResolver?.notifyChange(uri, null)
        return rowsUpdate
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        val db = dbOpenHelper.writableDatabase
        var rowsDeleted: Int

        when (sURIMatcher.match(uri)) {
            members -> rowsDeleted = db.delete(
                ClubOlimpusContract.MemberEntry.TABLE_NAME,
                selection,
                selectionArgs
            )
            memberId -> {
                val selection = ClubOlimpusContract.MemberEntry.KEY_ID + "=?"
                val selectionArgs = arrayOf(ContentUris.parseId(uri)).toString()
                rowsDeleted = db.delete(
                    ClubOlimpusContract.MemberEntry.TABLE_NAME, selection,
                    arrayOf(selectionArgs)
                )
            }
            else -> throw IllegalArgumentException("")
        }
        if (rowsDeleted != 0) context?.contentResolver?.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {

        return when (sURIMatcher.match(uri)) {
            members -> ClubOlimpusContract.MemberEntry.multipleItems
            memberId -> ClubOlimpusContract.MemberEntry.singleItems
            else -> throw java.lang.IllegalArgumentException("")
        }
    }
}