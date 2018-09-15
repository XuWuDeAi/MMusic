package main.zm.mmusic

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_main.*
import main.zm.mmusic.base.BaseActivity
import main.zm.mmusic.fragment.DownloadFragment
import main.zm.mmusic.fragment.HomeFragment
import main.zm.mmusic.fragment.LeaderboardFragment
import main.zm.mmusic.fragment.OtherFragment
import main.zm.mmusic.service.NetService
import android.media.MediaPlayer
import android.widget.Toast

import android.os.Environment.getExternalStorageDirectory
import android.os.Handler
import android.util.Log
import com.blankj.utilcode.util.PathUtils

import kotlinx.android.synthetic.main.activity_down_item.*
import com.hrb.library.MiniMusicView
import com.hrb.library.R.id.iv_next_btn
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_mini_music.*
import main.zm.mmusic.entity.MediaEntity
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.DownloadConfig

import zlc.season.rxdownload3.core.Status
import zlc.season.rxdownload3.extension.ApkInstallExtension
import zlc.season.rxdownload3.http.OkHttpClientFactoryImpl
import zlc.season.rxdownload3.notification.NotificationFactoryImpl
import java.net.URL
import android.os.Environment.getExternalStorageDirectory
import android.system.Os.remove
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.RemoteViews
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.zhy.http.okhttp.callback.FileCallBack
import com.zhy.http.okhttp.OkHttpUtils
import kotlinx.android.synthetic.main.layout_mini_music.view.*
import main.zm.mmusic.service.FileDataManage
import okhttp3.Call
import org.json.JSONObject
import zhy.com.highlight.HighLight
import zhy.com.highlight.interfaces.HighLightInterface
import zhy.com.highlight.position.OnBottomPosCallback
import zhy.com.highlight.position.OnLeftPosCallback
import zhy.com.highlight.position.OnRightPosCallback
import zhy.com.highlight.position.OnTopPosCallback
import zhy.com.highlight.shape.BaseLightShape
import zhy.com.highlight.shape.CircleLightShape
import zhy.com.highlight.shape.OvalLightShape
import zhy.com.highlight.shape.RectLightShape
import zhy.com.highlight.view.HightLightView
import java.io.File
import java.lang.Exception


class MainActivity : BaseActivity() {

    val mediaEntity: MediaEntity = MediaEntity()
    lateinit var mHightLight: HighLight
    var rView: RemoteViews? = null

    //    lateinit var mediaPlayer: MediaPlayer
    // 每一页就是一个Fragment
    var pages = arrayOfNulls<Fragment>(3)
    lateinit var manager: NotificationManager
    var isOpenApp = false//是已经启动app

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        pages[0] = LeaderboardFragment()
        pages[1] = HomeFragment()
        pages[2] = OtherFragment()
//        mediaPlayer = MediaPlayer()
        viewpager.offscreenPageLimit = 3
        initView()


//        val handler = Handler()
//        val updateThread = object : Runnable {
//            override fun run() {
//                // 获得歌曲现在播放位置并设置成播放进度条的值
//                if (mediaPlayer != null) {
//                    down_progress.progress = mediaPlayer.currentPosition
//
//                    // 每次延迟100毫秒再启动线程
//                    handler.postDelayed(this, 100)
//                }
//            }
//        }
//        updateThread.run()

//新手引导
        firstRun()
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NetService.openNoti(this)
        isOpenApp = true
    }


    // ViewPager支持
    private inner class MyViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        // 一共有几页
        override fun getCount(): Int {
            return pages.size
        }

        // 每一页的对象
        override fun getItem(position: Int): Fragment? {
            return pages[position]
        }

    }

    fun initView() {
        initviewpager()
        initTopButton()



        this.iv_next_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mediaEntity.lisdate?.let {
                    //                  toast(PathUtils.getExternalAppMusicPath())

                    createDownload(mediaEntity.fileURL!!, mediaEntity.lisdate!!.getJSONObject(mediaEntity.postion))

                }
            }

        })

        this.ll_layout.setOnTouchListener(object : View.OnTouchListener {

            internal var a = 0f
            internal var b = 0f
            override fun onTouch(v: View, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> Log.i("OnTouch", "ACTION_MOVE移动")
                    MotionEvent.ACTION_DOWN -> {
                        Log.i("OnTouch", "ACTION_DOWN开始触摸")
                        Log.i("OnTouch", e.x.toString() + "")
                        Log.i("OnTouch", e.y.toString() + "")
                        //获得开始触摸时的X值
                        a = e.x
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.i("OnTouch", "ACTION_UP抬起手指")
                        Log.i("OnTouch", e.x.toString() + "")
                        Log.i("OnTouch", e.y.toString() + "")
                        //获得抬起手指时的X值
                        b = e.x
                        //a<b证明右滑，否则左滑
                        if (a < b) {
                            if ((b - a) < 20) return true
                            val ss = when (b - a) {
                                in 20..99 -> 10000
                                in 100..200 -> 20000
                                else -> 30000
                            }
                            toast("+" + ss / 1000 + "s")
                            this@MainActivity.mmv_music.seekToMusic(this@MainActivity.mmv_music.pb_progress.progress + ss)

                        } else {


                            if ((a - b) < 20) return true
                            val ss = when (a - b) {
                                in 20..99 -> 10000
                                in 100..200 -> 20000
                                else -> 30000
                            }
                            toast("-" + ss / 1000 + "s")
                            this@MainActivity.mmv_music.seekToMusic(this@MainActivity.mmv_music.pb_progress.progress - ss)

//                            toast(this@MainActivity.mmv_music.musicDuration.toString())
                        }
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        Log.i("OnTouch", "ACTION_CANCEL手势取消")
                        Log.i("OnTouch", e.x.toString() + "")
                        Log.i("OnTouch", e.y.toString() + "")
                        //获得抬起手指时的X值
                        b = e.x
                        //a<b证明右滑，否则左滑
                        if (a < b) {
                            //TODO 这里执行右滑事件
                            Log.i("OnTouch", "右滑")
                        } else {
                            //TODO 这里执行左滑事件
                            Log.i("OnTouch", "左滑")
                        }
                    }
                    MotionEvent.ACTION_OUTSIDE -> {
                        Log.i("OnTouch", "ACTION_OUTSIDE超出UI范围")
                        Log.i("OnTouch", e.x.toString() + "")
                        Log.i("OnTouch", e.y.toString() + "")
                    }
                    else -> {
                    }
                }
                return true
            }
        })


    }

    private fun createDownload(url: String, item: JSONObject) {

        val fileName = NetService.getNetFileName(url)
        FileDataManage.getSPFileListDate(this@MainActivity)?.let {
            val jsonObject = JSONObject(it)
            jsonObject.put(fileName, item)
            FileDataManage.saveSPFileListDate(this@MainActivity, jsonObject)
        }
        if (FileUtils.isFileExists(PathUtils.getExternalAppMusicPath() + "/" + fileName)) {
            toast("你已经有这首歌啦")
            return
        }


        toast("下载开始")
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(object : FileCallBack(PathUtils.getExternalAppMusicPath(), fileName)//
                {
                    override fun onResponse(response: File?, id: Int) {
                        toast("下载完成")
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        toast("下载出错:" + e.toString())
                    }


                })
    }

    fun initviewpager() {
        // 给ViewPager设置Adapter
        val pagerAdapter = MyViewPagerAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter

        // 监听器：当滑动切换时，设置对应的标签高亮
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                paperChangebutton(position + 1)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun initTopButton() {

        ib_top1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ib_top1.setImageResource(R.drawable.ic_paiming)
                ib_top2.setImageResource(R.drawable.ic_home_activity)
                ib_top3.setImageResource(R.drawable.ic_download_activity)
                viewpager.currentItem = 0
            }
        })

        ib_top2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ib_top1.setImageResource(R.drawable.ic_paiming_activity)
                ib_top2.setImageResource(R.drawable.ic_home)
                ib_top3.setImageResource(R.drawable.ic_download_activity)
                viewpager.currentItem = 1
            }
        })
        ib_top3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                ib_top3.setImageResource(R.drawable.ic_download)
                ib_top1.setImageResource(R.drawable.ic_paiming_activity)
                ib_top2.setImageResource(R.drawable.ic_home_activity)
                viewpager.currentItem = 2

            }
        })

    }

    fun paperChangebutton(position: Int) {
        when (position) {
            1 -> ib_top1
            2 -> ib_top2
            else -> ib_top3
        }.performClick()

    }


    fun showNextKnownTipView() {
        mHightLight = HighLight(this@MainActivity)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(false)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果 而且使下方点击回调失效
                .anchor(findViewById(R.id.LL_activity_main))


        mHightLight.setOnLayoutCallback {
            //界面布局完成添加tipview
            mHightLight.addHighLight(R.id.mmv_music, R.layout.info_known, OnTopPosCallback(80F), OvalLightShape(3f, 3f, 20f));
            mHightLight.show()
        }

    }

    fun clickKnown(view: View) {
        if (mHightLight.isShowing && mHightLight.isNext)
        //如果开启next模式
        {
            mHightLight.next()
        } else {
            mHightLight.remove()
        }
    }

    fun firstRun() {
        val setting = getSharedPreferences("isOne", 0)
        val user_first = setting.getBoolean("FIRST", true)
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).commit()
            showNextKnownTipView()
        } else {
        }

    }

}




