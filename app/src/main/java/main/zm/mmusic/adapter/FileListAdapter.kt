package main.zm.mmusic.adapter

/**
 * Created by zm on 2018/9/3.
 */


import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.beardedhen.androidbootstrap.BootstrapThumbnail
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import main.zm.mmusic.MainActivity

import java.io.File

import main.zm.mmusic.R
import main.zm.mmusic.dialog.DeleFileDialog
import main.zm.mmusic.fragment.OtherFragment
import main.zm.mmusic.service.FileDataManage
import org.json.JSONObject
import java.net.URL


class FileListAdapter(private val mContext: MainActivity, var fileList: List<File>?, val otherFragment: OtherFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(url:String,it:JSONObject)
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_list_v, parent, false)
        return RecyclerHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerHolder).setData(position)
    }

    inner class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,View.OnLongClickListener {


        var mLinearLayout: LinearLayout
        var tv_filename: TextView
        var down_img: ImageView


        init {
            mLinearLayout = itemView.findViewById(R.id.item_layout)
            tv_filename = itemView.findViewById(R.id.tv_musicname)
            down_img = itemView.findViewById(R.id.down_img)
            mLinearLayout.setOnClickListener(this)
            mLinearLayout.setOnLongClickListener(this)

        }

        override fun onLongClick(p0: View?): Boolean {
            val win=DeleFileDialog()
            win.url=(fileList!!.get(layoutPosition).toString())
            win.show(otherFragment.mainActivity.supportFragmentManager,"")
           return true
        }

        override fun onClick(v: View) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(fileList!!.get(layoutPosition).toString(), FileDataManage.getFileKey(fileList!![position].toString(), otherFragment)!!)
            }
        }

        fun setData(position: Int) {
            FileDataManage.getFileKey(fileList!![position].toString(), otherFragment)?.let {
                tv_filename.text =   it.getString("title")

                Glide.with(mContext)
                        .load(it.getString("cover"))
                        .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                        .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                        .into(down_img)
            }



        }
    }

    override fun getItemCount(): Int {
        return if (fileList == null) 0 else fileList!!.size
    }
}
