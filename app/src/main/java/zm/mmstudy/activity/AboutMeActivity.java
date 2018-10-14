package zm.mmstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import zm.mmstudy.R;


public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);



    }
    // 响应扫钮被点击
    public void onClickeBlack( View view )
    {
        finish();
    }
}
