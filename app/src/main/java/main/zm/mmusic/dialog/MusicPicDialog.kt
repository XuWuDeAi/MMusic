package main.zm.mmusic.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_musicpic.*
import kotlinx.android.synthetic.main.layout_mini_music.*

import main.zm.mmusic.MainActivity
import main.zm.mmusic.R
import main.zm.mmusic.adapter.ListAdapter
import main.zm.mmusic.service.NetService
import android.graphics.Bitmap
import com.zhy.http.okhttp.callback.BitmapCallback
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.Call
import java.lang.Exception
import android.os.Environment.getExternalStorageDirectory
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.zhy.http.okhttp.callback.FileCallBack
import java.io.File


/**
 * Created by zm on 2018/7/27.
 */

class MusicPicDialog : DialogFragment() {

    lateinit var mainActivity: MainActivity
    lateinit var rl_filelist: RecyclerView
    lateinit var listAdapter: ListAdapter

    var listener: OnDialogListener? = null

    // 回调接口
    interface OnDialogListener {
        fun onDialogInput(option: String)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 创建View, 指定布局XML
        val view = inflater!!.inflate(R.layout.dialog_musicpic, container)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(mainActivity)
                .load(mainActivity.mediaEntity.musicUrl)
                .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                .into(iv_dlgmusicpic)

        bt_dlgmusicpic.setOnClickListener {

            var fileName = FileUtils.getFileName(mainActivity.mediaEntity.musicUrl)
            OkHttpUtils//
                    .get()//
                    .url(mainActivity.mediaEntity.musicUrl)//
                    .build()//
                    .execute(object : FileCallBack(PathUtils.getExternalAppPicturesPath(), fileName)//
                    {
                        override fun onResponse(response: File?, id: Int) {
                            mainActivity.toast("下载成功:" + PathUtils.getExternalAppPicturesPath() + fileName)
                        }

                        override fun onError(call: Call?, e: Exception?, id: Int) {
                            mainActivity.toast("下载失败:" + e!!.message.toString())
                        }

                    })

        }
    }

    override fun onStart() {
        super.onStart()

        // 当对话框显示时，调整对话框的窗口位置
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.WHITE))

            // 设置对话框的窗口显示
            val lp = window.attributes
            lp.dimAmount = 0.3f // 背景灰度
            lp.gravity = Gravity.CENTER // 靠上显示
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }
    }
}
