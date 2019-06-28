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
 * Created by tkru on 10/9/2017.
 */

public class Category_GridView_ImageAdapter extends ArrayAdapter<Category_Grid_Model> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Category_Grid_Model> mGridData = new ArrayList<Category_Grid_Model>();

    public Category_GridView_ImageAdapter(Context mContext, int layoutResourceId, ArrayList<Category_Grid_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Category_Grid_Model> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id.cat_sub_name);
            holder.url=(TextView)row.findViewById(R.id.cat_sub_url);
            holder.id=(TextView)row.findViewById(R.id.cat_sub_id);
            holder.imageView = (ImageView) row.findViewById(R.id.cat_sub_img);
            holder.prices=(TextView)row.findViewById(R.id.cat_sub_price);
            holder.desc=(TextView)row.findViewById(R.id.cat_sub_desc);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Category_Grid_Model item = mGridData.get(position);
        holder.titleTextView.setText(item.getCat_sub_name());
        holder.url.setText(item.getCat_sub_image());
        holder.id.setText(String.valueOf(item.getCat_sub_id()));
        holder.prices.setText(item.getCat_sub_price());
        holder.desc.setText(item.getCat_sub_desc());

        Glide.with(mContext)
                .load(item.getCat_sub_image())
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
        TextView url,id,prices,desc;
        ImageView imageView;
    }
}
