package zm.mmstudy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import zm.mmstudy.fragment.AmuseFragment;
import zm.mmstudy.fragment.AndroidFragment;
import zm.mmstudy.fragment.BlankFragment;

/**
 * Created by cui on 2017/9/5.
 */

public class MainFragAdapter extends FragmentPagerAdapter {

    String[] titles = {"语文", "数学", "英语", "物理", "化学", "生物"};
    String[] titlesTy = {"Android", "iOS", "前端", "拓展资源", "瞎推荐", "App"};
    // 每一页就是一个Fragment
    AndroidFragment[] pages = new  AndroidFragment[6];

    public MainFragAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
        pages[0]=new AndroidFragment();
        pages[0].setType(titlesTy[0]);

        pages[1]=new AndroidFragment();
        pages[1].setType(titlesTy[1]);

        pages[2]=new AndroidFragment();
        pages[2].setType(titlesTy[2]);

        pages[3]=new AndroidFragment();
        pages[3].setType(titlesTy[3]);

        pages[4]=new AndroidFragment();
        pages[4].setType(titlesTy[4]);

        pages[5]=new AndroidFragment();
        pages[5].setType(titlesTy[5]);
    }


    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
