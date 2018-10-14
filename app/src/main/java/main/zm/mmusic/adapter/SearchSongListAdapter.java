package main.zm.mmusic.adapter;

/**
 * Created by zm on 2018/9/3.
 */


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

import main.zm.mmusic.MainActivity;
import main.zm.mmusic.R;
import main.zm.mmusic.dialog.SearchMusicDialog;


public class SearchSongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SearchMusicDialog searchMusicDialog;
    private MainActivity mainActivity;
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

    public SearchSongListAdapter(SearchMusicDialog mContext) {
        this.searchMusicDialog=mContext;
        this.mainActivity = searchMusicDialog.mainActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_searchsonglist_v, parent, false);
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
        TextView tv_up;
        ImageView down_img;


        public RecyclerHolder(View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_musicname = itemView.findViewById(R.id.tv_musicname);
            tv_popularity = itemView.findViewById(R.id.tv_popularity);
            tv_up = itemView.findViewById(R.id.tv_up);
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
            try {
                tv_musicname.setText(listdate.getJSONObject(position).getString("title"));
                tv_up.setText(listdate.getJSONObject(position).getString("writer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Uri uri = null;
            try {
                uri = Uri.parse(listdate.getJSONObject(position).getString("cover"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            down_img.setImageURI(uri);

            Glide.with(mainActivity)
                    .load(uri)
                    .placeholder(R.mipmap.placeholder_disk_300)//图片加载出来前，显示的图片
                    .error(R.mipmap.placeholder_disk_300)//图片加载失败后，显示的图片
                    .into(down_img);


            try {

                Double count = listdate.getJSONObject(position).getInt("play_count") / 10000.0;
                tv_popularity.setText(String.format("%.1f", count) + "万");
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
