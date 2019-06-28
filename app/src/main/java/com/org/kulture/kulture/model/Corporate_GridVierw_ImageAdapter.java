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
 * Created by tkru on 10/11/2017.
 */

public class Corporate_GridVierw_ImageAdapter extends ArrayAdapter<Corporate_Wear_Model> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Corporate_Wear_Model> mGridData = new ArrayList<Corporate_Wear_Model>();

    public Corporate_GridVierw_ImageAdapter(Context mContext, int layoutResourceId, ArrayList<Corporate_Wear_Model> mGridData) {
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
    public void setGridData(ArrayList<Corporate_Wear_Model> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id.cor_title);
            holder.url = (TextView) row.findViewById(R.id.cor_url);
            holder.url2 = (TextView) row.findViewById(R.id.cor_url2);
            holder.id = (TextView) row.findViewById(R.id.cor_id);
            holder.imageView = (ImageView) row.findViewById(R.id.cor_img);
            holder.names = (TextView) row.findViewById(R.id.cor_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Corporate_Wear_Model item = mGridData.get(position);
        holder.titleTextView.setText(item.getCor_title());
        holder.url.setText(item.getCor_image());
        holder.url2.setText(item.getCor_image2());
        holder.id.setText(String.valueOf(item.getCor_id()));
        holder.names.setText(item.getCor_name());

        Glide.with(mContext)
                .load(item.getCor_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .skipMemoryCache(true)
                .placeholder(R.drawable.noimage)
                .animate(R.anim.bounce)
                .into(holder.imageView);
        return row;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView url,url2, id, names;
        ImageView imageView;
    }
}
