package main.zm.mmusic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        if (!ActivityUtils.isActivityExistsInStack(MainActivity::class.java)) {
            AppUtils.launchApp(packageName)
        }
        finish()
    }
}
