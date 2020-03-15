package com.example.clubolimpus.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

class ClubOlimpusContract {

    class MemberEntry(): BaseColumns {
        companion object {
            const val DATABASE_VERSION = 1
            const val TABLE_NAME = "members"

            const val KEY_ID = "_id"
            const val KEY_NAME = "name"
            const val KEY_SURNAME = "surname"
            const val KEY_SPORT = "sport"
            const val KEY_GENDER = "gender"

            const val Gender_None = 0
            const val Gender_Male = 1
            const val Gender_Female = 2

            val contentUri = Uri.withAppendedPath(baseContentUri, pathMembers)!!

            const val singleItems = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + authority + "/" + pathMembers
            const val multipleItems = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + authority + "/" + pathMembers
        }
    }

    companion object {
        const val DATA_BASE = "Olimpus"

        const val scheme = "content://"
        const val authority = "com.example.clubolimpus"
        const val pathMembers = "members"
        val baseContentUri = Uri.parse(scheme + authority)

    }
}
