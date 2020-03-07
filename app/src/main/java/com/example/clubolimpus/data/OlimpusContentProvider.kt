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

        when (sURIMatcher.match(uri)) {
            members -> {
                val id = db.insert(ClubOlimpusContract.MemberEntry.TABLE_NAME, null, values)
                if (id.toInt() == -1) {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
                    return null
                }
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

        when (sURIMatcher.match(uri)) {
            members -> return db.update(
                ClubOlimpusContract.MemberEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
            memberId -> {
                val selection = ClubOlimpusContract.MemberEntry.KEY_ID + "=?"
                val selectionArgs = arrayOf(arrayOf(ContentUris.parseId(uri)).toString())
                return db.update(
                    ClubOlimpusContract.MemberEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Не верный URI query")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        val db = dbOpenHelper.writableDatabase

        when (sURIMatcher.match(uri)) {
            members -> return db.delete(
                ClubOlimpusContract.MemberEntry.TABLE_NAME,
                selection,
                selectionArgs
            )
            memberId -> {
                val selection = ClubOlimpusContract.MemberEntry.KEY_ID + "=?"
                val selectionArgs = arrayOf(ContentUris.parseId(uri)).toString()
                return db.delete(
                    ClubOlimpusContract.MemberEntry.TABLE_NAME, selection,
                    arrayOf(selectionArgs)
                )
            }
            else -> throw IllegalArgumentException("")
        }
    }

    override fun getType(uri: Uri): String? {

        when (sURIMatcher.match(uri)) {
            members -> return ClubOlimpusContract.MemberEntry.multipleItems
            memberId -> return ClubOlimpusContract.MemberEntry.singleItems
            else -> throw java.lang.IllegalArgumentException("")
        }
    }

    companion object {
        const val members = 11
        const val memberId = 22
    }
}