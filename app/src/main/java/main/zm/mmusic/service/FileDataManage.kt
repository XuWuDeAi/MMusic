package main.zm.mmusic.service

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import com.blankj.utilcode.util.FileUtils
import main.zm.mmusic.MainActivity
import main.zm.mmusic.fragment.OtherFragment
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by zm on 2018/9/11.
 */
object FileDataManage {

    fun getSPFileListDate(mainActivity: MainActivity): String? {
        val sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString("fileList", "{}")
    }

    fun saveSPFileListDate(mainActivity: MainActivity, jsonObject: JSONObject) {
        // 打开SharedPreference对象，这个对象相当于一个配置文件，里面可以存放多个配置项
        val sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE)
        // 如果要修改配置项，需要先获取一个editor对象
        val editor = sharedPref.edit()
        // 把配置项存进去
        editor.putString("fileList", jsonObject.toString())
        // 提交保存
        editor.commit()
    }

    //根据文件本地全路径获取对应的文件名字
    fun getFileKey(url: String, otherFragment: OtherFragment): JSONObject? {
        otherFragment.fileKey.getJSONObject(FileUtils.getFileName(url))?.let {
            return it
        }
        return null
    }

}