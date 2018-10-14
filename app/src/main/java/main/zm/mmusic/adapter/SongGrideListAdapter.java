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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import main.zm.mmusic.MainActivity;
import main.zm.mmusic.R;
import main.zm.mmusic.entity.SongGrideEntity;


public class SongGrideListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainActivity mContext;
    public List<SongGrideEntity> listdate;
    private OnItemClickListener mOnItemClickListener;

    public void setListdate(List listdate) {
        this.listdate = listdate;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SongGrideListAdapter(Context mContext) {
        this.mContext = (MainActivity) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song_gride, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecyclerHolder) holder).setData(position);
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout item_layout;
        TextView tv_songname;
        ImageView iv_song;


        public RecyclerHolder(View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_songname = itemView.findViewById(R.id.tv_songname);
            item_layout.setOnClickListener(this);
            iv_song = itemView.findViewById(R.id.iv_song);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getLayoutPosition());
            }
        }

        public void setData(int position) {
            tv_songname.setText(listdate.get(position).getSongName());
            Glide.with(mContext)
                    .load(listdate.get(position).getSongIm())
                    .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                    .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                    .thumbnail( 0.1f )//略缩图只显示原图10%
                    .into(iv_song);
        }
    }

    @Override
    public int getItemCount() {
        return listdate == null ? 0 : listdate.size();
    }
}
