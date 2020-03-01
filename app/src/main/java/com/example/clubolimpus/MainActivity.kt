package com.example.clubolimpus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.clubolimpus.data.ClubOlimpusContract
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton = floatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity,AddMemberActivity::class.java)
            startActivity(intent)
        }
    }
}
