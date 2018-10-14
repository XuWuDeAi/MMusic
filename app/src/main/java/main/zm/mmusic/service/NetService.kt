package main.zm.mmusic.service

import android.app.*
import android.net.Uri
import com.bumptech.glide.Glide
import org.json.JSONObject
import com.zhy.http.okhttp.callback.StringCallback
import com.zhy.http.okhttp.OkHttpUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_mini_music.*

import main.zm.mmusic.R
import main.zm.mmusic.dialog.SongListDialog
import main.zm.mmusic.fragment.HomeFragment
import main.zm.mmusic.fragment.LeaderboardFragment
import okhttp3.Call
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

import android.content.Intent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.NotificationCompat

import android.widget.RemoteViews
import kotlinx.android.synthetic.main.layout_mini_musictis.*
import main.zm.mmusic.MainActivity
import main.zm.mmusic.MessageActivity
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.zhy.http.okhttp.callback.BitmapCallback
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import main.zm.mmusic.broadcast.ServiceReceiver
import main.zm.mmusic.dialog.SearchMusicDialog
import okhttp3.Request


/**
 * Created by zm on 2018/9/9.
 */
object NetService {

    lateinit var Netmainactivity: MainActivity


    fun getTopMusic(leaderboardFragment: LeaderboardFragment) {
        val url = "https://www.bilibili.com/audio/music-service-c/web/song/of-menu?sid=10627&pn=1&ps=100"
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val resJson = JSONObject(response)
                        val data = resJson.getJSONObject("data").getJSONArray("data")
                        leaderboardFragment.listAdapter!!.setListdate(data)
                        leaderboardFragment.listAdapter!!.notifyDataSetChanged()
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        leaderboardFragment.mainActivity.toast("网络请求超时")
                    }
                })
    }

    fun getRecommendMusicList(homeFragment: HomeFragment) {
        val url = "https://www.bilibili.com/audio/music-service-c/web/menu/hit?pn=1&ps=50"
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val resJson = JSONObject(response)
                        val data = resJson.getJSONObject("data").getJSONArray("data")
                        homeFragment.listAdapter.listdate = data
                        homeFragment.listAdapter.notifyDataSetChanged()
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        homeFragment.mainActivity.toast("网络请求超时")
                    }
                })
    }

    fun getMusicListDialog(songListDialog: SongListDialog, sid: Int) {
        val url = "https://www.bilibili.com/audio/music-service-c/web/song/of-menu?sid=$sid&pn=1&ps=100"
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val resJson = JSONObject(response)
                        val data = resJson.getJSONObject("data").getJSONArray("data")
                        songListDialog.listAdapter.listdate = data
                        songListDialog.listAdapter.notifyDataSetChanged()
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        songListDialog.mainActivity.toast("网络请求超时")
                    }
                })
    }

    fun getNetMusic(sid: Int, mainActivity: MainActivity, itj: JSONObject, postion: Int, listDate: JSONArray) {
        //如果在播放中，立刻暂停。
        if (mainActivity.mmv_music.isPlaying()) {
            mainActivity.mmv_music.pausePlayMusic()
        }
        mainActivity.mmv_music.changeControlBtnState(true)

        setBottomMedia(mainActivity, itj.getString("cover"), itj.getString("title"), itj.getString("author"), postion, listDate)
        notiupdate(mainActivity, itj.getString("title"), itj.getString("author"))
        OkHttpUtils
                .get()//
                .url(itj.getString("cover"))//
                .build()//
                .execute(object : BitmapCallback() {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                    }

                    override fun onResponse(response: Bitmap?, id: Int) {
                        val builder = NotificationCompat.Builder(mainActivity)
                        builder.setSmallIcon(R.drawable.miao)
                        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
                        rv.setTextViewText(R.id.tv_music_title, itj.getString("title"))//修改自定义View中的歌名
                        rv.setTextViewText(R.id.tv_music_author, itj.getString("author"))
                        rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)

                        val intent = Intent(mainActivity, MessageActivity::class.java)
                        val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
                        builder.setContentIntent(ma)//设置点击过后跳转的activity

                        var buttonPlayIntent = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST) //----设置通知栏按钮广播
                        var pendButtonPlayIntent = PendingIntent.getBroadcast(mainActivity, 0, buttonPlayIntent, 0);
                        rv.setOnClickPendingIntent(R.id.bt_notilast, pendButtonPlayIntent)//----设置对应的按钮ID监控


                        //修改自定义View中的图片(两种方法)
                        rv.setImageViewBitmap(R.id.iv_noti_music_icon, response)
                        mainActivity.rView = rv
                        builder.setContent(rv)
                        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
                            var channelID = "2"
                            var channelName = "channel_name";
                            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
                            if (notificationManager != null) {
                                notificationManager.createNotificationChannel(channel)
                                builder.setChannelId(channelID);
                                builder.build()
                            }
                            notificationManager.notify(2, builder.build())
                        } else {            //弹出通知栏 8.0以下系统弹出方式
                            if (notificationManager != null) {
                                notificationManager.notify(2, builder.build());
                            }
                        }
                    }
                })

        val url = "https://www.bilibili.com/audio/music-service-c/web/url?sid=$sid&privilege=2&quality=2"
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val resJson = JSONObject(response)
                        val musicUrl = resJson.getJSONObject("data").getJSONArray("cdns").get(0).toString()
                        try {
//                            mainActivity.mediaPlayer.reset();// 重置
//                            mainActivity.mediaPlayer.setDataSource(musicUrl)
//                            mainActivity.mediaPlayer.prepare()
//                            mainActivity.mediaPlayer.start()

                            mainActivity.mediaEntity.fileURL = musicUrl
                            OpenMusic(mainActivity, musicUrl)
                        } catch (e: IOException) {
                            mainActivity.toast("音乐不存在")
                        }

                    }


                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        mainActivity.toast("网络请求超时")
                    }
                })
    }

    fun gettopIBSid(buttonid: Int): Int {
        return when (buttonid) {
            0 -> 10624
            1 -> 10627
            2 -> 10628
            3 -> 10632
            4 -> 30473
            5 -> 10630
            6 -> 10631
            7 -> 10629
            8 -> 30474
            9 -> 10633
            10 -> 30472
            11 -> 10634
            else -> 10629
        }

    }

    fun setBottomMedia(mainActivity: MainActivity, picUrl: String, musicName: String, author: String, postion: Int, listDate: JSONArray?) {
//        mainActivity.down_img_buttom.setImageURI(Uri.parse(picUrl))
//        mainActivity.down_top_text.text = musicName

        mainActivity.mediaEntity.musicUrl = picUrl
        Glide.with(mainActivity)
                .load(picUrl)
                .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                .into(mainActivity.iv_music_icon)

        mainActivity.mmv_music.setTitleText(musicName)
        mainActivity.mmv_music.setAuthor(author)
        mainActivity.mediaEntity.postion = postion
        mainActivity.mediaEntity.lisdate = listDate

        mainActivity.rView?.let {
            it.setTextViewText(R.id.tv_music_title, musicName)
        }//修改通知栏View中的歌名

    }

    fun OpenMusic(mainActivity: MainActivity, musicUrl: String) {
        mainActivity.mmv_music.startPlayMusic(musicUrl)
    }

    fun getNetFileName(s1: String): String {
        val start = s1.lastIndexOf("?")  // 从开头查找
        if (start >= 0) {
            val end = s1.lastIndexOf("/", start)   // 这是indexOf()的另一用法，第2个参数表示从start位置向后查找
            if (end >= 0) {

                val name = s1.substring(end + 1, start)
                return name
            }
        }
        return ""
    }

    fun openLocationMusic(musicUrl: String, mainActivity: MainActivity, itj: JSONObject) {
        //如果在播放中，立刻暂停。
        if (mainActivity.mmv_music.isPlaying()) {
            mainActivity.mmv_music.pausePlayMusic()
        }
        mainActivity.mmv_music.changeControlBtnState(true)
        setBottomMedia(mainActivity, itj.getString("cover"), itj.getString("title"), itj.getString("author"), 0, null)
        mainActivity.mediaEntity.fileURL = null
        OpenMusic(mainActivity, musicUrl)
//        setNoti(mainActivity, it.getString("title"), it.getString("author"), it.getString("cover"))
//        var intentOne = Intent(mainActivity, notiService::class.java)
//        intentOne.putExtra("picUrl", it.getString("cover"))
//        intentOne.putExtra("musicName", it.getString("title"))
//        intentOne.putExtra("author", it.getString("author"))
//        mainActivity.startService(intentOne)

        notiupdate(mainActivity, itj.getString("title"), itj.getString("author"))
        OkHttpUtils
                .get()//
                .url(itj.getString("cover"))//
                .build()//
                .execute(object : BitmapCallback() {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                    }

                    override fun onResponse(response: Bitmap?, id: Int) {
                        val builder = NotificationCompat.Builder(mainActivity)
                        builder.setSmallIcon(R.drawable.miao)
                        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
                        rv.setTextViewText(R.id.tv_music_title, itj.getString("title"))//修改自定义View中的歌名
                        rv.setTextViewText(R.id.tv_music_author, itj.getString("author"))
                        rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)

                        val intent = Intent(mainActivity, MessageActivity::class.java)
                        val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
                        builder.setContentIntent(ma)//设置点击过后跳转的activity

                        var buttonPlayIntent = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST) //----设置通知栏按钮广播
                        var pendButtonPlayIntent = PendingIntent.getBroadcast(mainActivity, 0, buttonPlayIntent, 0);
                        rv.setOnClickPendingIntent(R.id.bt_notilast, pendButtonPlayIntent)//----设置对应的按钮ID监控


                        //修改自定义View中的图片(两种方法)
                        rv.setImageViewBitmap(R.id.iv_noti_music_icon, response)
                        mainActivity.rView = rv
                        builder.setContent(rv)

                        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
                            var channelID = "2"
                            var channelName = "channel_name";
                            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
                            if (notificationManager != null) {
                                notificationManager.createNotificationChannel(channel)
                                builder.setChannelId(channelID);
                                builder.build()
                            }
                            notificationManager.notify(2, builder.build())
                        } else {            //弹出通知栏 8.0以下系统弹出方式
                            if (notificationManager != null) {
                                notificationManager.notify(2, builder.build());
                            }
                        }
                    }
                })

    }

    fun openNoti(mainActivity: MainActivity) {


        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
//        rv.setTextViewText(R.id.tv_music_title, "泡沫")//修改自定义View中的歌名

        ////////////////////////
        val intent = Intent(mainActivity, MessageActivity::class.java)
        val ma = PendingIntent.getActivity(mainActivity, 0, intent, 0)
        builder.setContentIntent(ma)//设置点击过后跳转的activity

        var buttonPlayIntent = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST) //----设置通知栏按钮广播
        var pendButtonPlayIntent = PendingIntent.getBroadcast(mainActivity, 0, buttonPlayIntent, 0);
        rv.setOnClickPendingIntent(R.id.bt_notilast, pendButtonPlayIntent);//----设置对应的按钮ID监控

        var buttonPlayIntent2 = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT) //----设置通知栏按钮广播
        var pendButtonPlayIntent2 = PendingIntent.getBroadcast(mainActivity, 1, buttonPlayIntent2, 0);
        rv.setOnClickPendingIntent(R.id.bt_notinext, pendButtonPlayIntent2)//----设置对应的按钮ID监控

        var buttonPlayIntent3 = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY) //----设置通知栏按钮广播
        var pendButtonPlayIntent3 = PendingIntent.getBroadcast(mainActivity, 1, buttonPlayIntent3, 0);
        rv.setOnClickPendingIntent(R.id.bt_noticontrolmusic, pendButtonPlayIntent3)//----设置对应的按钮ID监控

        var buttonPlayIntent4 = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_MUSICTYPE) //----设置通知栏按钮广播
        var pendButtonPlayIntent4 = PendingIntent.getBroadcast(mainActivity, 1, buttonPlayIntent4, 0);
        rv.setOnClickPendingIntent(R.id.iv_notimusictype, pendButtonPlayIntent4)//----设置对应的按钮ID监控

        //修改自定义View中的图片(两种方法)
//        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        mainActivity.rView = rv
        builder.setContent(rv)
        var notification = builder.build()
        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(2, notification)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
            var channelID = "2"
            var channelName = "channel_name";
            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelID);
            }
            notificationManager.notify(2, builder.build())
        } else {            //弹出通知栏 8.0以下系统弹出方式
            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }
        }
    }


//    fun setNoti(mainActivity: MainActivity, musicName: String, author: String, picUrl: String) {
//
//        Glide.with(mainActivity).load(picUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
//            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
//                val builder = NotificationCompat.Builder(mainActivity)
//                builder.setSmallIcon(R.drawable.miao)
//                val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
//                rv.setTextViewText(R.id.tv_music_title, musicName)//修改自定义View中的歌名
//                rv.setTextViewText(R.id.tv_music_author, author)
//                rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)
//
//                val intent = Intent(mainActivity, MessageActivity::class.java)
//                val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
//                builder.setContentIntent(ma)//设置点击过后跳转的activity
//
//                var buttonPlayIntent = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST) //----设置通知栏按钮广播
//                var pendButtonPlayIntent = PendingIntent.getBroadcast(mainActivity, 0, buttonPlayIntent, 0);
//                rv.setOnClickPendingIntent(R.id.bt_notilast, pendButtonPlayIntent)//----设置对应的按钮ID监控
//
//
//                //修改自定义View中的图片(两种方法)
//                rv.setImageViewBitmap(R.id.iv_noti_music_icon, resource)
//                mainActivity.rView = rv
//                builder.setContent(rv)
//                val notification = builder.build()
//                val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.notify(2, notification)
//            }
//        }) //方法中设置asBitmap可以设置回调类型
//
//    }

    fun updateNoti(mainActivity: MainActivity, isplay: Boolean) {


        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)

        val intent = Intent(mainActivity, MessageActivity::class.java)
        val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
        builder.setContentIntent(ma)//设置点击过后跳转的activity
        if (!isplay) {
            //修改自定义View中的图片(两种方法)
            rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_play)
        } else {
            // (两种方法)
            rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)
        }

        mainActivity.rView = rv
        builder.setContent(rv)

        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
            var channelID = "2"
            var channelName = "channel_name";
            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelID);
                builder.build()
            }
            notificationManager.notify(2, builder.build())
        } else {            //弹出通知栏 8.0以下系统弹出方式
            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }
        }
    }

    fun updateNotiType(mainActivity: MainActivity) {


        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
        mainActivity.musicType
        if ((mainActivity.musicType + 1) > 2) {
            mainActivity.musicType = 0
        } else {
            mainActivity.musicType++
        }

        when {
            mainActivity.musicType == 0 -> rv.setImageViewResource(R.id.iv_notimusictype, R.drawable.ic_onereturn)
            mainActivity.musicType == 1 -> rv.setImageViewResource(R.id.iv_notimusictype, R.drawable.ic_cycle)
            mainActivity.musicType == 2 -> rv.setImageViewResource(R.id.iv_notimusictype, R.drawable.ic_random)
        }

        mainActivity.rView = rv
        builder.setContent(rv)

        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
            var channelID = "2"
            var channelName = "channel_name";
            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelID);
                builder.build()
            }
            notificationManager.notify(2, builder.build())
        } else {            //弹出通知栏 8.0以下系统弹出方式
            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }
        }


    }

    fun getSearchMusic(keyWord: String, searchMusicDialog: SearchMusicDialog) {

        val url = "http://api.bilibili.com/audio/music-service-c/s?appkey=1d8b6e7d45233436&build=5310300&keyword=$keyWord&mobi_app=android&page=1&pagesize=20&platform=android&search_type=music&ts=1537275674&sign=787ee9b465fc72752010bfe96a697106"
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        var datalist = JSONObject(response.toString()).getJSONObject("data").getJSONArray("result")
                        searchMusicDialog.mainActivity.toast("搜索结果:" + datalist.length() + "个")
                        searchMusicDialog.listAdapter.listdate = datalist
                        searchMusicDialog.listAdapter.notifyDataSetChanged()
                    }


                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        searchMusicDialog.mainActivity.toast("网络请求超时")
                    }
                })

    }

    fun notiupdate(mainActivity: MainActivity, title: String, author: String) {
        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
        rv.setTextViewText(R.id.tv_music_title, title)//修改自定义View中的歌名
        rv.setTextViewText(R.id.tv_music_author, author)
        rv.setImageViewResource(R.id.bt_noticontrolmusic, R.drawable.mini_btn_pause)

        val intent = Intent(mainActivity, MessageActivity::class.java)
        val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
        builder.setContentIntent(ma)//设置点击过后跳转的activity

        var buttonPlayIntent = Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST) //----设置通知栏按钮广播
        var pendButtonPlayIntent = PendingIntent.getBroadcast(mainActivity, 0, buttonPlayIntent, 0);
        rv.setOnClickPendingIntent(R.id.bt_notilast, pendButtonPlayIntent)//----设置对应的按钮ID监控


        //修改自定义View中的图片(两种方法)
        mainActivity.rView = rv
        builder.setContent(rv)
        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上弹出通知状态栏
            var channelID = "2"
            var channelName = "channel_name";
            var channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelID);
                builder.build()
            }
            notificationManager.notify(2, builder.build())
        } else {            //弹出通知栏 8.0以下系统弹出方式
            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }
        }
    }
}