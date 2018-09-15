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
        ib_homtop1.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop2.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop3.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop4.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop5.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop6.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop7.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
        ib_homtop8.setOnClickListener {
            var songListDialog = SongListDialog()
            songListDialog.show(mainActivity.supportFragmentManager, NetService.gettopIBSid(it.id).toString())
        }
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
