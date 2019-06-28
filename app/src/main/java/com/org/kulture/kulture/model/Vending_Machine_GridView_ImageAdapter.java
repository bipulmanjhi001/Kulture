package com.org.kulture.kulture.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.R;

import java.util.ArrayList;

/**
 * Created by tkru on 11/3/2017.
 */

public class Vending_Machine_GridView_ImageAdapter extends ArrayAdapter<Vending_Machine_Grid_Model> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Vending_Machine_Grid_Model> mGridData = new ArrayList<Vending_Machine_Grid_Model>();

    public Vending_Machine_GridView_ImageAdapter(Context mContext, int layoutResourceId, ArrayList<Vending_Machine_Grid_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<Vending_Machine_Grid_Model> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.url = (TextView) row.findViewById(R.id.vending_machine_thumb_url);
            holder.url2 = (TextView) row.findViewById(R.id.vending_machine_large_url);
            holder.imageView = (ImageView) row.findViewById(R.id.vending_machine_thumb);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Vending_Machine_Grid_Model item = mGridData.get(position);
        holder.url.setText(item.getThumb_image());
        holder.url2.setText(item.getLarge_image());

        Glide.with(mContext)
                .load(item.getThumb_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .fitCenter()
                .skipMemoryCache(true)
                .placeholder(R.drawable.loading)
                .animate(R.anim.bounce)
                .into(holder.imageView);
        return row;
    }

    private static class ViewHolder {
        TextView url, url2;
        ImageView imageView;
    }
}