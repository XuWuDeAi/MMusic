package main.zm.mmusic.adapter;

/**
 * Created by zm on 2018/9/3.
 */


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;

import main.zm.mmusic.MainActivity;
import main.zm.mmusic.R;


public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainActivity mContext;
    public JSONArray listdate;
    private OnItemClickListener mOnItemClickListener;

    public void setListdate(JSONArray listdate) {
        this.listdate = listdate;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SongListAdapter(MainActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_songlist_v, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder) holder).setData(position);
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout item_layout;
        TextView tv_musicname;
        TextView tv_popularity;

        ImageView down_img;


        public RecyclerHolder(View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_musicname = itemView.findViewById(R.id.tv_musicname);
            tv_popularity = itemView.findViewById(R.id.tv_popularity);
            item_layout.setOnClickListener(this);
            down_img = itemView.findViewById(R.id.down_img);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getLayoutPosition());
            }
        }

        public void setData(int position) {
            String title = null;
            try {
                title = listdate.getJSONObject(position).getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv_musicname.setText(title);
            Uri uri = null;
            try {
                uri = Uri.parse(listdate.getJSONObject(position).getString("cover"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            down_img.setImageURI(uri);

            Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                    .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                    .into(down_img);


            try {
                tv_popularity.setText(listdate.getJSONObject(position).getJSONObject("statistic").getInt("play")+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listdate == null ? 0 : listdate.length();
    }

}
