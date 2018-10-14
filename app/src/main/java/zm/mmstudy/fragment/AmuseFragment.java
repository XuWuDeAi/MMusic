package zm.mmstudy.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.rxbus.RxBus;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;

import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.activity.OpenWebActivity;
import zm.mmstudy.adapter.ListAdapter;
import zm.mmstudy.service.MainService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmuseFragment extends Fragment {

    public View contentView;
    private RecyclerView mRecyclerView;
    public MainActivity mainActivity;
    public RefreshLayout refreshLayout;
    JSONArray LisdateCP;

    public AmuseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 创建界面
        contentView = inflater.inflate(R.layout.fragment_amuse, container, false);
        refreshLayout = contentView.findViewById(R.id.refreshLayout);
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                MainService.getGankToday(mainActivity, AmuseFragment.this);
            }

        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadMore(false);//传入false表示加载失败

            }
        });

        // 注册带 tag 为 "my tag" 的 String 类型事件
        RxBus.getDefault().subscribe(this, "webUrl", new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                String url = null;
                try {
                    url = LisdateCP.getJSONObject(Integer.parseInt(s)).getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(mainActivity, OpenWebActivity.class);
                intent.putExtra("webUrl", url);
                startActivity(intent);
            }


        });
        initData(null);
        MainService.getGankToday(mainActivity, AmuseFragment.this);
        return contentView;
    }

    public void initData(final JSONArray Lisdate) {
        LisdateCP=Lisdate;

        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ListAdapter listAdapter = new ListAdapter(mainActivity, AmuseFragment.this, true, Lisdate);
        mRecyclerView.setAdapter(listAdapter);

    }


}
