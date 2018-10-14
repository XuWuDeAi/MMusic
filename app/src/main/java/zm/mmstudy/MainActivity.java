package zm.mmstudy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.flyco.tablayout.SlidingTabLayout;

import zm.mmstudy.adapter.MainFragAdapter;
import zm.mmstudy.fragment.AmuseFragment;
import zm.mmstudy.fragment.HomeFragment;
import zm.mmstudy.fragment.MeFragment;
import zm.mmstudy.service.MainService;
import zm.mmstudy.view.NoScrollViewPager;

public class MainActivity extends AppCompatActivity {


    private SlidingTabLayout tabLayout1;
    ViewPager viewPager;
    // 每一页就是一个Fragment
    Fragment[] pages = new Fragment[3];

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        MainService.loadPiFu(this);//换皮肤


        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.vp_main);
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        pages[0] = new HomeFragment();
        pages[1] = new AmuseFragment();
        pages[2] = new MeFragment();
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        // 监听器：当滑动切换时，设置对应的标签高亮
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(navigation.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // ViewPager支持
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // 一共有几页
        @Override
        public int getCount() {
            return pages.length;
        }

        // 每一页的对象
        @Override
        public Fragment getItem(int position) {
            return pages[position];
        }

    }
    public void toast(String it) {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show();
        return;
    }

}
