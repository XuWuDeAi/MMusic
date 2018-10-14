package main.zm.mmusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import main.zm.mmusic.MainActivity
import main.zm.mmusic.service.NetService

/**
 * Created by zm on 2018/9/24.
 */

class ServiceReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        val mainActivity = context as MainActivity
        val action = intent.action
        if (action == NOTIFICATION_ITEM_BUTTON_LAST) {//----通知栏播放按钮响应事件
            mainActivity.mediaEntity.lisdate?.let {

                var pos = mainActivity.mediaEntity.postion
                if ((pos - 1) < 0) {
                    mainActivity.mediaEntity.postion = it.length() - 1
                    NetService.getNetMusic(mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion).getInt("id"), mainActivity, mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion), mainActivity.mediaEntity.postion, mainActivity.mediaEntity.lisdate!!)
                } else {
                    mainActivity.mediaEntity.postion--
                    NetService.getNetMusic(mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion).getInt("id"), mainActivity, mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion), mainActivity.mediaEntity.postion, mainActivity.mediaEntity.lisdate!!)
                }

            }
        } else if (action == NOTIFICATION_ITEM_BUTTON_PLAY) {//----通知栏播放按钮响应事件

            //如果在播放中，立刻暂停。
            if (mainActivity.mmv_music.isPlaying()) {
                mainActivity.mmv_music.pausePlayMusic()
                mainActivity.mmv_music.changeControlBtnState(false)
                NetService.updateNoti(mainActivity, false)
            } else {

                mainActivity.mmv_music.resumePlayMusic()
                mainActivity.mmv_music.changeControlBtnState(true)
                NetService.updateNoti(mainActivity, true)
            }


        } else if (action == NOTIFICATION_ITEM_BUTTON_NEXT) {//----通知栏下一首按钮响应事件
            mainActivity.mediaEntity.lisdate?.let {

                var pos = mainActivity.mediaEntity.postion
                if ((pos + 1) == it.length()) {
                    mainActivity.mediaEntity.postion = 0
                    NetService.getNetMusic(mainActivity.mediaEntity.lisdate!!.getJSONObject(0).getInt("id"), mainActivity, mainActivity.mediaEntity.lisdate!!.getJSONObject(0), 0, mainActivity.mediaEntity.lisdate!!)
                } else {
                    mainActivity.mediaEntity.postion++
                    NetService.getNetMusic(mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion).getInt("id"), mainActivity, mainActivity.mediaEntity.lisdate!!.getJSONObject(mainActivity.mediaEntity.postion), mainActivity.mediaEntity.postion, mainActivity.mediaEntity.lisdate!!)
                }

            }


        } else if (action == NOTIFICATION_ITEM_BUTTON_MUSICTYPE) {
            NetService.updateNotiType(mainActivity)
        }
    }

    companion object {
        val NOTIFICATION_ITEM_BUTTON_LAST = "com.example.notification.ServiceReceiver.last"//----通知栏上一首按钮
        val NOTIFICATION_ITEM_BUTTON_PLAY = "com.example.notification.ServiceReceiver.play"//----通知栏播放按钮
        val NOTIFICATION_ITEM_BUTTON_NEXT = "com.example.notification.ServiceReceiver.next"//----通知栏下一首按钮
        val NOTIFICATION_ITEM_BUTTON_MUSICTYPE = "com.example.notification.ServiceReceiver.mussictype"//----通知栏下一首按钮
    }

}


