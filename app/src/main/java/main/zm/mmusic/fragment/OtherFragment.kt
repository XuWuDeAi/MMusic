package main.zm.mmusic.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils


import org.json.JSONArray

import main.zm.mmusic.MainActivity
import main.zm.mmusic.R
import main.zm.mmusic.adapter.FileListAdapter
import main.zm.mmusic.service.FileDataManage
import main.zm.mmusic.service.NetService
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class OtherFragment : Fragment() {

    lateinit var conTenView: View
    lateinit var mainActivity: MainActivity
    lateinit var rl_filelist: RecyclerView
    lateinit var listAdapter: FileListAdapter
    lateinit var fileListDate: JSONArray
    var fileKey = JSONObject()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mainActivity = (context as MainActivity?)!!

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        conTenView = inflater!!.inflate(R.layout.fragment_other, container, false)
//        val bd_filer = conTenView.findViewById<BootstrapDropDown>(R.id.bd_filer)
        rl_filelist = conTenView.findViewById(R.id.rl_filelist)

//        bd_filer.setOnDropDownItemClickListener { parent, v, id ->
//            val textView = v as TextView
//            mainActivity.toast(textView.text.toString())
//            bd_filer.text = textView.text
//            //                listAdapter.fileList = MainService.fileDropDown(textView.getText().toString(), mainActivity);
//            //                listAdapter.notifyDataSetChanged();
//        }

        initDate()


        FileDataManage.getSPFileListDate(mainActivity)?.let {
            fileKey = JSONObject(it)
        }


        return conTenView
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            updataList()

        } else {

        }
    }

    internal fun initDate() {
        val layoutManager = LinearLayoutManager(mainActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rl_filelist.layoutManager = layoutManager
        //测试数据
        //        mainActivity.getExternalFilesDir(null).getAbsolutePath();
        //        File dataDir = mainActivity.getExternalFilesDir("data");

        val fileListDate = FileUtils.listFilesInDir(PathUtils.getExternalAppMusicPath())
        listAdapter = FileListAdapter(mainActivity, fileListDate, this)
        rl_filelist.adapter = listAdapter
        listAdapter.setOnItemClickListener(object : FileListAdapter.OnItemClickListener {
            override fun onItemClick(url: String, it: JSONObject) {
                NetService.openLocationMusic(url, mainActivity, it)
            }

        })

    }

    fun updataList() {
        FileDataManage.getSPFileListDate(mainActivity)?.let {
            fileKey = JSONObject(it)
            listAdapter.fileList = FileUtils.listFilesInDir(PathUtils.getExternalAppMusicPath())

        }
        listAdapter.notifyDataSetChanged()
    }
}// Required empty public constructor
