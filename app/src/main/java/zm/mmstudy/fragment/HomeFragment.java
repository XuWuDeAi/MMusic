package zm.mmstudy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.adapter.MainFragAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public View contentView;
    ViewPager viewPager;
    MainActivity mainActivity;
    SlidingTabLayout tabLayout1;


    public HomeFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = contentView.findViewById(R.id.vp_home);
        tabLayout1=contentView.findViewById(R.id.tabLayout_1);

        String[] titles = {"Android", "iOS", "前端", "资源", "推荐", "demon"};
        MainFragAdapter adapter = new MainFragAdapter(getChildFragmentManager(), titles);
        viewPager.setAdapter(adapter);
        tabLayout1.setViewPager(viewPager);
        return contentView;
    }

}
