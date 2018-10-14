package main.zm.mmusic.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button

import com.beardedhen.androidbootstrap.BootstrapButton
import com.blankj.utilcode.util.FileUtils

import main.zm.mmusic.MainActivity
import main.zm.mmusic.R
import main.zm.mmusic.fragment.OtherFragment


/**
 * Created by zm on 2018/7/27.
 */

class DeleFileDialog : DialogFragment() {

    lateinit var url: String
    lateinit var mainActivity: MainActivity
    var listener: OnDialogListener? = null


    // 回调接口
    interface OnDialogListener {
        fun onDialogInput(option: String)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 创建View, 指定布局XML
        val view = inflater!!.inflate(R.layout.dialog_hualun, container)

        // 点击按钮时，关闭对话框
        val button = view.findViewById<BootstrapButton>(R.id.id_ok)
        button.setOnClickListener {
            dismiss() // 关闭对话框
            FileUtils.deleteFile(url)
            (mainActivity.pages[2] as OtherFragment).updataList()
        }

        return view
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
            lp.gravity = Gravity.BOTTOM // 靠下显示
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }
    }
}
