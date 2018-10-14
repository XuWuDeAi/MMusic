package zm.mmstudy.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.carbs.android.library.MDDialog;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import zm.mmstudy.BuildConfig;
import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.fragment.AmuseFragment;
import zm.mmstudy.fragment.AndroidFragment;
import zm.mmstudy.fragment.HomeFragment;


/**
 * Created by zm on 2018/8/3.
 */

public class MainService {

    public static void initView(MainActivity mainActivity) throws Exception {

    }

    public static void loadPiFu(MainActivity mainActivity) {
        String option = getLaoPo(mainActivity);
        if (option.equals("天依")) {
            mainActivity.setTheme(R.style.tyTheme);
        } else if (option.equals("阿绫")) {
            mainActivity.setTheme(R.style.alTheme);
        } else if (option.equals("言和")) {
            mainActivity.setTheme(R.style.yhTheme);
        } else if (option.equals("星尘")) {
            mainActivity.setTheme(R.style.xcTheme);
        } else if (option.equals("心华")) {
            mainActivity.setTheme(R.style.xhTheme);
        } else if (option.equals("蕾姆")) {
            mainActivity.setTheme(R.style.lmTheme);
        }
    }

    public static void setPiFu(MainActivity mainActivity, String option) {
        if (option.equals("天依")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        } else if (option.equals("阿绫")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        } else if (option.equals("言和")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        } else if (option.equals("星尘")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        } else if (option.equals("心华")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        } else if (option.equals("蕾姆")) {
            saveLaoPo(mainActivity, option);
            mainActivity.recreate();
        }
    }

    public static void saveLaoPo(MainActivity mainActivity, String laoPo) {
        // 打开SharedPreference对象，这个对象相当于一个配置文件，里面可以存放多个配置项
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        // 如果要修改配置项，需要先获取一个editor对象
        SharedPreferences.Editor editor = sharedPref.edit();
        // 把配置项存进去
        editor.putString("laopo", laoPo);
        // 提交保存
        editor.commit();
    }

    public static String getLaoPo(MainActivity mainActivity) {
        // 打开SharedPreference对象，这个对象相当于一个配置文件，里面可以存放多个配置项
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("laopo", "蕾姆");
    }

    public static void showVersionDialog(MainActivity mainContext) {
        String context;
        context = "已经是最新版本了";

        final String[] messages = new String[]{
        };
        new MDDialog.Builder(mainContext)
                .setMessages(messages)
                .setTitle(context)
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                })
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                    }
                })
                .setWidthMaxDp(600)
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    public static Double getLoctionVersion() {
        String versionName = BuildConfig.VERSION_NAME;
        return Double.parseDouble(versionName);
    }


    public static void getResVersion(final MainActivity mainActivity) {
        OkHttpUtils
                .postString()
                .url("http://47.106.162.236:80/CloundUniversity/GetVersion.api")
                .content("")
                .mediaType(MediaType.parse("text/plain; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mainActivity.toast("网络请求超时");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        showVersionDialog(mainActivity);
                    }
                });
        return;
    }

    public static void getGankToday(final MainActivity mainActivity, final AmuseFragment amuseFragment) {
//        OkHttpUtils
//                .postString()
//                .url("https://gank.io/api/today")
//                .content("")
//                .mediaType(MediaType.parse("text/plain; charset=utf-8"))
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//
//                    }
//                    @Override
//                    public void onResponse(String response, int id) {
//
//                    }
//                });

//        String url = "https://gank.io/api/today";
//
//        OkhttpUtil.okHttpGet(url, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//
//                amuseFragment.refreshLayout.finishLoadMore(false);
//                mainActivity.toast(e.getMessage()+call.toString());
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject resJson =new JSONObject(response);
//                    amuseFragment.refreshLayout.finishLoadMore(true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        String url = "https://gank.io/api/today";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mainActivity.toast("网络请求超时");

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject resJson = new JSONObject(response);
                            JSONObject results = resJson.getJSONObject("results");
                            JSONArray Android = results.getJSONArray("Android");
                            JSONArray openDate = results.getJSONArray("拓展资源");
                            JSONArray iOS = results.getJSONArray("iOS");
                            JSONArray recommend = results.getJSONArray("瞎推荐");
                            Android = joinJSONArray(Android, iOS);
                            Android = joinJSONArray(Android, openDate);
                            Android = joinJSONArray(Android, recommend);
                            amuseFragment.initData(Android);
                            amuseFragment.refreshLayout.finishRefresh(true);//传入false表示刷新失败
                        } catch (JSONException e) {
                            e.printStackTrace();
                            amuseFragment.refreshLayout.finishRefresh(false);//传入false表示刷新失败
                        }
                    }
                });

    }

    //合并两个JSONArray
    public static JSONArray joinJSONArray(JSONArray mData, JSONArray array) {
        StringBuffer buffer = new StringBuffer();
        try {
            int len = mData.length();
            for (int i = 0; i < len; i++) {
                JSONObject obj1 = (JSONObject) mData.get(i);
                if (i == len - 1)
                    buffer.append(obj1.toString());
                else
                    buffer.append(obj1.toString()).append(",");
            }
            len = array.length();
            if (len > 0)
                buffer.append(",");
            for (int i = 0; i < len; i++) {
                JSONObject obj1 = (JSONObject) array.get(i);
                if (i == len - 1)
                    buffer.append(obj1.toString());
                else
                    buffer.append(obj1.toString()).append(",");
            }
            buffer.insert(0, "[").append("]");
            return new JSONArray(buffer.toString());
        } catch (Exception e) {
        }
        return null;
    }

    public static void getGankAn(final MainActivity mainActivity, final AndroidFragment androidFragment, final int page) {
        String url = "https://gank.io/api/search/query/listview/category/"+androidFragment.type+"/count/15/page/"+page;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mainActivity.toast("网络请求超时");
                        androidFragment.refreshLayout.finishLoadMore(false);//传入false表示加载失败
                        androidFragment.refreshLayout.finishRefresh(false);//传入false表示刷新失败
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject resJson = new JSONObject(response);
                            JSONArray results = resJson.getJSONArray("results");
                            if (page!=1)
                            {
                             androidFragment.toMoreData(results);
                                androidFragment.refreshLayout.finishLoadMore(true);//传入false表示加载失败
                            }
                            androidFragment.initData(results);
                            androidFragment.refreshLayout.finishRefresh(true);//传入false表示刷新失败
                            androidFragment.page++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            androidFragment.refreshLayout.finishRefresh(false);//传入false表示刷新失败
                        }
                    }
                });

    }
}
