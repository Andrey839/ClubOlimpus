package com.example.clubolimpus

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.clubolimpus.data.ClubOlimpusContract.MemberEntry
import com.example.clubolimpus.data.MemberCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        const val memberLoader = 33
    }

    var memberCursorAdapter: MemberCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton = floatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddMemberActivity::class.java)
            startActivity(intent)
        }

        memberCursorAdapter = MemberCursorAdapter(this, null, false)
        listView.adapter = memberCursorAdapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, AddMemberActivity::class.java).apply {
                    val uri = ContentUris.withAppendedId(MemberEntry.contentUri, id)
                    data = uri
                }
                startActivity(intent)
            }
        supportLoaderManager.initLoader(memberLoader, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
            MemberEntry.KEY_ID,
            MemberEntry.KEY_NAME,
            MemberEntry.KEY_SURNAME,
            MemberEntry.KEY_SPORT
        )

        return CursorLoader(this, MemberEntry.contentUri, projection, null, null, null)

    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        memberCursorAdapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        memberCursorAdapter?.swapCursor(null)
    }

}
