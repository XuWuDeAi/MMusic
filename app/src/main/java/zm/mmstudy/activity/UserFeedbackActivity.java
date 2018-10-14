package zm.mmstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.Call;
import okhttp3.MediaType;
import zm.mmstudy.R;

public class UserFeedbackActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        Button buttonBack = findViewById(R.id.id_button_back);
        final TextView feedbackText = findViewById(R.id.id_editText_feedback);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_user_feedback);
                String feedbackString = feedbackText.getText().toString();
                if (feedbackString.equals("")) {
                    toast("内容不能为空");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("context", feedbackString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OkHttpUtils
                            .postString()
                            .url("http://47.106.162.236:80/CloundUniversity/UserFeedback.api")
                            .content(jsonObject.toString())
                            .mediaType(MediaType.parse("text/plain; charset=utf-8"))
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    toast("网络请求超时");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    toast("发射成功");
                                }
                            });


                }

                finish();
            }
        });


    }

    // 响应扫钮被点击
    public void onClickeBlack(View view) {
        finish();
    }

    public void toast(String it) {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show();

        return;
    }
}
