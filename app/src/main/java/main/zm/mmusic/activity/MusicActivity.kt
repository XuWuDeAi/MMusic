package main.zm.mmusic.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_music.*
import main.zm.mmusic.R

class MusicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)


        id_button_back.setOnClickListener {
            finish()
        }

        bt_noticontrolmusic.setOnClickListener {

            bt_noticontrolmusic.setImageResource(R.drawable.ic_pasue)
        }
    }
}
