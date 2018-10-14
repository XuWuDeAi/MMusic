package zm.mmstudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import zm.mmstudy.R;


public class OpenWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openweb);
        // 初始化WebView
        WebView webView = (WebView) findViewById(R.id.WebView);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        // 获取传进来的参数,加载目标网址
        Intent intent = getIntent();
        String website = intent.getStringExtra("webUrl");
        if(website != null)
        {
            webView.loadUrl( website);
        }


        ImageButton btn = findViewById(R.id.bt_openback);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
