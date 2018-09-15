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
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils

import main.zm.mmusic.MainActivity
import main.zm.mmusic.R
import main.zm.mmusic.adapter.ListAdapter
import main.zm.mmusic.service.NetService


/**
 * Created by zm on 2018/7/27.
 */

class SongListDialog : DialogFragment() {

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
        val view = inflater!!.inflate(R.layout.dialog_songlist, container)
        val ll_dialog = view.findViewById<LinearLayout>(R.id.ll_dialog)
        rl_filelist = view.findViewById(R.id.rl_filelist)

        //        //设置dialog与屏幕沾满
        //        ll_dialog.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()));
        initListView()
        NetService.getMusicListDialog(this, tag.toInt())
        return view
    }

    private fun initListView() {

        val layoutManager = LinearLayoutManager(mainActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL//设置垂直
        rl_filelist.layoutManager = layoutManager

        listAdapter = ListAdapter(mainActivity)
        rl_filelist.adapter = listAdapter
        listAdapter.setOnItemClickListener { position -> NetService.getNetMusic(listAdapter!!.listdate.getJSONObject(position).getInt("id"),mainActivity, listAdapter!!.listdate.getJSONObject(position),position,listAdapter.listdate) }
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
            lp.gravity = Gravity.TOP // 靠上显示
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ScreenUtils.getScreenHeight() - 210 - SizeUtils.dp2px(56f)
            window.attributes = lp
        }
    }
}
