package com.org.kulture.kulture.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.kulture.kulture.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tkru on 8/12/2017.
 */

public class OrderList extends ArrayAdapter<OrderItems> implements Filterable {

    private LayoutInflater vi;
    private int Resource;
    private ViewHolder holder;
    private Context contexts;
    private List<OrderItems> orderList;
    public OrderList(Context context, int resource, ArrayList<OrderItems> objects) {
        super(context, resource, objects);

        vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        orderList = objects;
        contexts=context;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public OrderItems getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder = new ViewHolder();
            holder.qty = (TextView) v.findViewById(R.id.qty);
            holder.extra_id = (TextView) v.findViewById(R.id.extra_id);
            holder.Title = (TextView) v.findViewById(R.id.ord_title);
            holder.order_id = (TextView) v.findViewById(R.id.order_id);
            holder.paid_price = (TextView) v.findViewById(R.id.paid_price);
            holder.imageView = (ImageView) v.findViewById(R.id.list_image);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();// like a fragment class all items added to viewholder
        }
        holder.imageView.setImageResource(R.drawable.noimage);//default image for loading time

        holder.Title.setText(orderList.get(position).getName());
        holder.qty.setText("Qty : "+orderList.get(position).getQty());
        holder.extra_id.setText(String.valueOf(orderList.get(position).getProduct_Id()));
        holder.order_id.setText("Order ID : "+String.valueOf(orderList.get(position).getId()));
        holder.paid_price.setText(orderList.get(position).getTotal());

        return v;
    }
        private class ViewHolder {
            ImageView imageView;
            TextView Title, order_id, qty;
            TextView paid_price, extra_id;
        }
    }