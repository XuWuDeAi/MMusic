package main.zm.mmusic.service

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import main.zm.mmusic.MainActivity
import main.zm.mmusic.MessageActivity
import main.zm.mmusic.R
import main.zm.mmusic.broadcast.ServiceReceiver


/**
 * Created by zm on 2018/9/25.
 */

class notiService : IntentService(notiService::class.java.name) {

    lateinit var mainActivity: MainActivity

    override fun onHandleIntent(intent: Intent?) {
        mainActivity = NetService.Netmainactivity

        Observable.create(ObservableOnSubscribe<String> { it ->



            it.onNext("")
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {

                    intent?.let {
                        Glide.with(mainActivity
                        ).load(it.getStringExtra("picUrl")).asBitmap().into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                                val builder = NotificationCompat.Builder(mainActivity)
                                builder.setSmallIcon(R.drawable.miao)
                                val rv = RemoteViews(mainActivity.packageName, R.layout.layout_mini_musictis)
                                rv.setTextViewText(R.id.tv_music_title, it.getStringExtra("musicName"))//修改自定义View中的歌名
                                rv.setTextViewText(R.id.tv_music_author, it.getStringExtra("author"))
                                rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)

                                val intent = Intent(mainActivity, MessageActivity::class.java)
                                val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
                                builder.setContentIntent(ma)//设置点击过后跳转的activity

                                //修改自定义View中的图片(两种方法)
                                rv.setImageViewBitmap(R.id.iv_noti_music_icon, resource)
                                mainActivity.rView = rv
                                builder.setContent(rv)
                                val notification = builder.build()
                                val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.notify(2, notification)
                            }
                        }) //方法中设置asBitmap可以设置回调类型 }

                    }

                }
    }
}

