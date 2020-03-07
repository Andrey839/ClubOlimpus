package com.example.clubolimpus.data


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper as SQLiteOpenHelper1

class OlimpusDbOpenHelper(
    context: Context?
) : SQLiteOpenHelper1(
    context,
    ClubOlimpusContract.DATA_BASE,
    null,
    ClubOlimpusContract.MemberEntry.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTablePerson =
            "CREATE TABLE " + ClubOlimpusContract.MemberEntry.TABLE_NAME + "(" +
                    ClubOlimpusContract.MemberEntry.KEY_ID + " INTEGER PRIMARY KEY," +
                    ClubOlimpusContract.MemberEntry.KEY_NAME + " TEXT," +
                    ClubOlimpusContract.MemberEntry.KEY_SURNAME + " TEXT," +
                    ClubOlimpusContract.MemberEntry.KEY_GENDER + " INTEGER NOT NULL," +
                    ClubOlimpusContract.MemberEntry.KEY_SPORT + " TEXT" + ")"
        db?.execSQL(createTablePerson)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + ClubOlimpusContract.MemberEntry.TABLE_NAME)
        onCreate(db)
    }

}