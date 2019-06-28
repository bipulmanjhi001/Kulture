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
 * Created by Bipul on 26-04-2017.
 */
public class Product_GridView_ImageAdapter extends ArrayAdapter<Product_Grid_Model> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Product_Grid_Model> mGridData = new ArrayList<Product_Grid_Model>();

    public Product_GridView_ImageAdapter(Context mContext, int layoutResourceId, ArrayList<Product_Grid_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Product_Grid_Model> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id.cat_name);
            holder.url=(TextView)row.findViewById(R.id.cat_url);
            holder.id=(TextView)row.findViewById(R.id.cat_id);
            holder.imageView = (ImageView) row.findViewById(R.id.cat_img);
            holder.slug=(TextView)row.findViewById(R.id.cat_slug);
            holder.count=(TextView)row.findViewById(R.id.cat_count);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Product_Grid_Model item = mGridData.get(position);
        holder.titleTextView.setText(item.getCat_name());
        holder.url.setText(item.getCat_image());
        holder.id.setText(item.getCat_id());
        holder.slug.setText(item.getCat_slug());
        holder.count.setText(item.getCat_count());

        Glide.with(mContext)
                .load(item.getCat_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .animate(R.anim.bounce)
                .into(holder.imageView);
        return row;
    }
   private static class ViewHolder {
        TextView titleTextView;
        TextView url,id,slug,count;
        ImageView imageView;
    }
}
