package zm.mmstudy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.rxbus.RxBus;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.fragment.AmuseFragment;


/**
 * Function:RecyclerView适配器
 * Created by xuzhuyun on 2017/11/16.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private AmuseFragment fContext;
    private boolean isVertical;
    //    private int[] imgResId;
    private JSONArray lisDate;





    public ListAdapter(Context mContext, AmuseFragment fContext, boolean isVertical, JSONArray lisDate) {
        this.mContext = mContext;
        this.isVertical = isVertical;
        this.lisDate = lisDate;
        this.fContext = fContext;
//        this.imgResId = imgResId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(isVertical ? R.layout.item_list_v : R.layout.item_list_h, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder) holder).setData(position);
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout ll_onclick;
        SimpleDraweeView mImageView;
        TextView mTextView;
        TextView tv_type;

        public RecyclerHolder(View itemView) {
            super(itemView);
            ll_onclick = itemView.findViewById(R.id.ll_onclick);
            mImageView = itemView.findViewById(R.id.item_img);
            mTextView = itemView.findViewById(R.id.item_txt);
            tv_type = itemView.findViewById(R.id.tv_type);
            ll_onclick.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // 发送带 tag 为 "webUrl" 的 String 类型事件
            RxBus.getDefault().post(String.valueOf(getLayoutPosition()), "webUrl");
        }

        public void setData(int position) {
            JSONObject jb = null;
            String desc = null;
            String type = null;
            String imgUrl = null;

            try {
                jb = (JSONObject) lisDate.get(position);
                type = jb.getString("type");
                desc = jb.getString("desc");
                if (jb.has("images")) {
                    imgUrl = jb.getJSONArray("images").getString(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv_type.setText(type);
            mTextView.setText(desc);
            if (imgUrl != null) {
                Uri uri = Uri.parse(imgUrl);
                mImageView.setImageURI(uri);
            }


        }
    }

    @Override
    public int getItemCount() {
        return lisDate == null ? 0 : lisDate.length();
    }
}