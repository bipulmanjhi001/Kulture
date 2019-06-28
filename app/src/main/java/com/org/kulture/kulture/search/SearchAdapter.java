package com.org.kulture.kulture.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.org.kulture.kulture.R;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mCategory;
    private LayoutInflater mLayoutInflater;
    private boolean mIsFilterList;

    public SearchAdapter(Context context, ArrayList<String> category, boolean isFilterList) {
        this.mContext = context;
        this.mCategory =category;
        this.mIsFilterList = isFilterList;
    }


    public void updateList(ArrayList<String> filterList, boolean isFilterList) {
        this.mCategory = filterList;
        this.mIsFilterList = isFilterList;
        notifyDataSetChanged ();
    }

    @Override
    public int getCount() {
        return mCategory.size();
    }

    @Override
    public String getItem(int position) {
        return mCategory.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if(v==null){

            holder = new ViewHolder();

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = mLayoutInflater.inflate(R.layout.list_item_search, parent, false);
            holder.txtCategory = (TextView)v.findViewById(R.id.txt_country);
            v.setTag(holder);
        } else{

            holder = (ViewHolder) v.getTag();
        }

        holder.txtCategory.setText(mCategory.get(position));

        Drawable searchDrawable,recentDrawable;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            searchDrawable = mContext.getResources().getDrawable(R.mipmap.ic_magnify_grey600_24dp, null);
            recentDrawable = mContext.getResources().getDrawable(R.mipmap.ic_backup_restore_grey600_24dp, null);

        } else {
            searchDrawable = mContext.getResources().getDrawable(R.mipmap.ic_magnify_grey600_24dp);
            recentDrawable = mContext.getResources().getDrawable(R.mipmap.ic_backup_restore_grey600_24dp);
        }
        if(mIsFilterList) {
            holder.txtCategory.setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
        }else {
            holder.txtCategory.setCompoundDrawablesWithIntrinsicBounds(recentDrawable, null, null, null);

        }
        return v;
    }

}

class ViewHolder{
     TextView txtCategory;
}





