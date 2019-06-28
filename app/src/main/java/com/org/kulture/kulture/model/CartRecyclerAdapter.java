package com.org.kulture.kulture.model;


import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.CartActivity;
import com.org.kulture.kulture.R;

import java.util.ArrayList;

/**
 * Created by tkru on 11/6/2017.
 */

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.MyViewHolder> {
    private ArrayList<CartModel> cartModels =new ArrayList<CartModel>();
    private ArrayList<String> cartids =new ArrayList<String>();
    String id;
    int possses;
    CartActivity cart;

    /**
     * View holder class
     * */

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView cart_qty,product_id;
        public TextView cart_name,product_size;
        public TextView cart_total_price,delete;
        public ImageView cart_image,bin;
        public LinearLayout parentLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            cart_qty= (TextView) view.findViewById(R.id.qty_cart);
            cart_name=(TextView)view.findViewById(R.id.cart_product);
            cart_total_price=(TextView)view.findViewById(R.id.cart_price);
            cart_image=(ImageView) view.findViewById(R.id.get_cart_image);
            product_size=(TextView)view.findViewById(R.id.product_size);
            product_id=(TextView)view.findViewById(R.id.product_id);
            bin=(ImageView)view.findViewById(R.id.bin_img);
            bin.setOnClickListener(this);
            delete=(TextView)view.findViewById(R.id.text_remove);
            delete.setOnClickListener(this);
            parentLinearLayout=(LinearLayout)view.findViewById(R.id.parentLinearLayout);
        }
        @Override
        public void onClick(View v) {
             possses = getAdapterPosition(); // gets item position
            if (possses!= RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                id=cartids.get(possses);
                FragmentTransaction ft = cart.getFragmentManager().beginTransaction();
                RemoveSingleItem frag = new RemoveSingleItem();
                frag.addSomeString(id);
                frag.show(ft, "txn_tag");
            }
        }
    }
    public CartRecyclerAdapter(CartActivity cartActivity,ArrayList<CartModel> cartModelss,ArrayList<String> idss) {
        this.cartModels = cartModelss;
        this.cartids=idss;
        this.cart=cartActivity;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CartModel cartModel=cartModels.get(position);
        holder.cart_qty.setText(cartModel.getCart_qty());
        holder.cart_name.setText(cartModel.getCart_name());
        holder.cart_total_price.setText(cartModel.getCart_total_price());
        holder.product_size.setText(cartModel.getCart_size());
        holder.product_id.setText(cartModel.getProduct_id());

        Glide.with(cart)
                .load(cartModel.getCart_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .skipMemoryCache(true)
                .animate(R.anim.bounce)
                .into(holder.cart_image);
    }
    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_cart_details,parent, false);
        return new MyViewHolder(v);
    }

}
