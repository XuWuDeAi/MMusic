package main.zm.mmusic.service

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

import android.app.PendingIntent
import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.NotificationCompat

import android.widget.RemoteViews
import kotlinx.android.synthetic.main.layout_mini_musictis.*
import zlc.season.rxdownload3.helper.getPackageName
import main.zm.mmusic.MainActivity
import main.zm.mmusic.MessageActivity


/**
 * Created by zm on 2018/9/9.
 */
object NetService {

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

    fun getNetMusic(sid: Int, mainActivity: MainActivity, it: JSONObject, postion: Int, listDate: JSONArray) {
        //如果在播放中，立刻暂停。
        if (mainActivity.mmv_music.isPlaying()) {
            mainActivity.mmv_music.pausePlayMusic()
        }
        mainActivity.mmv_music.changeControlBtnState(true)

        setBottomMedia(mainActivity, it.getString("cover"), it.getString("title"), it.getString("author"), postion, listDate)
        setNoti(mainActivity,it.getString("title"),it.getString("author"),it.getString("cover"))
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
            R.id.ib_homtop1 -> 10624
            R.id.ib_homtop2 -> 10627
            R.id.ib_homtop3 -> 10628
            R.id.ib_homtop4 -> 10632
            R.id.ib_homtop5 -> 30473
            R.id.ib_homtop6 -> 10630
            R.id.ib_homtop7 -> 10631
            else -> 10629
        }

    }

    fun setBottomMedia(mainActivity: MainActivity, picUrl: String, musicName: String, author: String, postion: Int, listDate: JSONArray?) {
//        mainActivity.down_img_buttom.setImageURI(Uri.parse(picUrl))
//        mainActivity.down_top_text.text = musicName

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
            it.setTextViewText(R.id.tv_music_title, musicName) }//修改通知栏View中的歌名

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


    fun openLocationMusic(musicUrl: String, mainActivity: MainActivity, it: JSONObject) {
        //如果在播放中，立刻暂停。
        if (mainActivity.mmv_music.isPlaying()) {
            mainActivity.mmv_music.pausePlayMusic()
        }
        mainActivity.mmv_music.changeControlBtnState(true)
        setBottomMedia(mainActivity, it.getString("cover"), it.getString("title"), it.getString("author"), 0, null)
        mainActivity.mediaEntity.fileURL = null
        OpenMusic(mainActivity, musicUrl)
        setNoti(mainActivity,it.getString("title"),it.getString("author"),musicUrl)

    }

    fun openNoti(mainActivity: MainActivity) {
        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
//        rv.setTextViewText(R.id.tv_music_title, "泡沫")//修改自定义View中的歌名

        val intent = Intent(mainActivity, MessageActivity::class.java)
        val ma = PendingIntent.getActivity(mainActivity, 0, intent, 0)
        builder.setContentIntent(ma)//设置点击过后跳转的activity
        //修改自定义View中的图片(两种方法)
//        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        mainActivity.rView = rv
        builder.setContent(rv)
        val notification = builder.build()
        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    fun setNoti(mainActivity: MainActivity,musicName: String,author:String,picUrl: String) {
        val builder = NotificationCompat.Builder(mainActivity)
        builder.setSmallIcon(R.drawable.miao)
        val rv = RemoteViews(mainActivity.getPackageName(), R.layout.layout_mini_musictis)
        rv.setTextViewText(R.id.tv_music_title, musicName)//修改自定义View中的歌名
        rv.setTextViewText(R.id.tv_music_author, author)

        val intent = Intent(mainActivity, MessageActivity::class.java)
        val ma = PendingIntent.getActivity(mainActivity, 3, intent, 1)
        builder.setContentIntent(ma)//设置点击过后跳转的activity
        //修改自定义View中的图片(两种方法)
//        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);

        mainActivity.rView = rv
        builder.setContent(rv)
        val notification = builder.build()
        val notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }


}