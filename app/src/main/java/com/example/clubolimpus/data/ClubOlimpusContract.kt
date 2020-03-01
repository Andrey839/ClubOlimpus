package com.example.clubolimpus.data

import android.provider.BaseColumns

class ClubOlimpusContract{
    class MemberEntry(): BaseColumns {
        companion object {
            const val TABLE_NAME = BaseColumns._ID

            const val KEY_ID = "id"
            const val KEY_NAME = "name"
            const val KEY_SURNAME = "surname"
            const val KEY_GROUP = "group"
            const val KEY_GENDER = "gender"

            const val Gender_None = 0
            const val Gender_Male =1
            const val Gender_Female = 2
        }
    }
}
