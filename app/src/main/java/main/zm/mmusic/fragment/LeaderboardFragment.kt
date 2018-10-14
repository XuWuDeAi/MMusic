package main.zm.mmusic.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import main.zm.mmusic.MainActivity

import main.zm.mmusic.R
import main.zm.mmusic.adapter.ListAdapter
import main.zm.mmusic.service.NetService
import java.io.IOException
import java.net.URL


/**
 * A simple [Fragment] subclass.
 */
class LeaderboardFragment : Fragment() {

    lateinit var conTentView: View
    var listAdapter: ListAdapter? =null
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = (context as MainActivity?)!!
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        conTentView = inflater!!.inflate(R.layout.fragment_leaderboard, container, false)
        return conTentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListView()
        NetService.getTopMusic(this)



    }

    private fun initListView() {
        val layoutManager = LinearLayoutManager(mainActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_leaderboard.layoutManager = layoutManager

        listAdapter = ListAdapter(mainActivity)
        rv_leaderboard.adapter = listAdapter
        listAdapter!!.setOnItemClickListener { position ->
            run {
                NetService.getNetMusic(listAdapter!!.listdate.getJSONObject(position).getInt("id"),mainActivity, listAdapter!!.listdate.getJSONObject(position),position, listAdapter!!.listdate)
//                NetService.setBottomMedia(mainActivity, listAdapter!!.listdate.getJSONObject(position).getString("cover"), listAdapter!!.listdate.getJSONObject(position).getString("title"),listAdapter!!.listdate.getJSONObject(position).getString("author"))
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
                NetService.getTopMusic(this)
        } else {

        }
    }

}// Required empty public constructor
