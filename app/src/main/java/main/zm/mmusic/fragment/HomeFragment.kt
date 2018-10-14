package main.zm.mmusic.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import main.zm.mmusic.MainActivity

import main.zm.mmusic.R
import main.zm.mmusic.adapter.ListAdapter
import android.support.v7.widget.GridLayoutManager
import main.zm.mmusic.adapter.SongListAdapter
import main.zm.mmusic.dialog.SongListDialog
import main.zm.mmusic.service.NetService
import android.widget.Toast
import main.zm.mmusic.adapter.SongGrideListAdapter
import main.zm.mmusic.entity.SongGrideEntity
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var listAdapter: SongListAdapter

    lateinit var conTentView: View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        conTentView = inflater!!.inflate(R.layout.fragment_home, container, false)
        return conTentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListView()

//        ib_homtop1.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop2.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop3.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop4.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop5.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop6.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop7.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop8.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop9.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop10.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop11.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }
//        ib_homtop12.setOnClickListener {
//            var songListDialog = SongListDialog()
//            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
//        }

        initSongGrideListData()
        rv_recommendlist.isNestedScrollingEnabled = false
        rv_recommendlist.setHasFixedSize(true)

    }

    private fun initListView() {

        val layoutManager = LinearLayoutManager(mainActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL//设置垂直
        rv_recommendlist.layoutManager = layoutManager

        listAdapter = SongListAdapter(mainActivity)
        rv_recommendlist.adapter = listAdapter
        listAdapter.setOnItemClickListener { position ->
            run {
                val songListDialog = SongListDialog()
                songListDialog.show(mainActivity.supportFragmentManager, listAdapter.listdate.getJSONObject(position).getInt("menuId").toString())
            }
        }
    }

    private fun initSongGrideListData() {
        val layoutManager = GridLayoutManager(mainActivity, 4)
        rv_songgridelist.setLayoutManager(layoutManager)
        //测试数据
        val list = ArrayList<SongGrideEntity>()
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/a32c1ed4f6ec3f74f8240f4486a750dda3a509e5.jpg", "新歌榜"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/bc735b32ab123f7ddc602b9194defae2cd66062f.jpg", "热歌榜"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/58782504cefb307878d12422fd365ed7f971fad1.jpg", "原创榜"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/13954c2c6f1758d7b2dcfc572b4c733dd1acfc10.jpg", "古风人声"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/744ceed6c99d219f839349ef9682220b47cc05b3.jpg", "ACG"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/7dd5d629f50481e22e2f5eced510f5fbd89f5233.jpg", "日文人声"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/ce8b1646cbbd5513de7976ba81d0fd9c340c1899.jpg", "VOCALOIO"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/f547e3b96d3f283eaa6a3a071de22092fb5101f8.jpg", "中文人声"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/fc78897d4873d749c2e523370fb18ab91a4a63a6.jpg", "英文人声"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/257b09c0ca00915704f52f37744bec159c8de364.jpg", "鬼畜"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/ac44ce52d4343862c769f0add8209d4829990ffc.jpg", "有声节目"))
        list.add(SongGrideEntity("https://i0.hdslb.com/bfs/music/c40b18ae1da108191a6da6894b810cee1f6d3885.jpg", "纯音乐"))
        val songListDialog = SongGrideListAdapter(mainActivity)
        songListDialog.listdate=list
        songListDialog.setOnItemClickListener { position ->
            run {
               mainActivity.toast(position.toString())
            }
        }
        rv_songgridelist.adapter = songListDialog

        songListDialog.setOnItemClickListener { position -> run { var songListDialog = SongListDialog()
           songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(position).toString()) }}
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (listAdapter == null) {
                return
            }
            if (listAdapter!!.listdate == null || listAdapter!!.listdate.length() <= 0)
                NetService.getRecommendMusicList(this)

        } else {

        }
    }
}// Required empty public constructor
