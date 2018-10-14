package main.zm.mmusic.base

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by zm on 2018/9/9.
 */
open class BaseActivity : AppCompatActivity() {

    fun toast(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        return
    }


}