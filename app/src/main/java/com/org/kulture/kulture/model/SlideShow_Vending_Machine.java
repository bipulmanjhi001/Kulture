package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.R;

import java.util.ArrayList;

/**
 * Created by tkru on 11/3/2017.
 */

public class SlideShow_Vending_Machine extends DialogFragment {
    ArrayList<String> list = new ArrayList<String>();
    String getimg;
    ImageView img;
    int count;
    LinearLayout outside_click;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG2);
        list =getArguments().getStringArrayList("imagearray");
        getimg =getArguments().getString("clickimg");
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slideshow_vending_machine, container, false);
        img=(ImageView)root.findViewById(R.id.slide_img);
        ImageView left_arrow=(ImageView)root.findViewById(R.id.left_arrow);
        final ImageView right_arrow=(ImageView)root.findViewById(R.id.right_arrow);

        outside_click=(LinearLayout)root.findViewById(R.id.outside_click);
        outside_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Glide.with(this)
                .load(getimg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .fitCenter()
                .skipMemoryCache(true)
                .animate(R.anim.bounce)
                .placeholder(R.drawable.noimage)
                .into(img);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0 && count >=1) {
                    count--;
                        Glide.with(getActivity())
                                .load(list.get(count))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .fitCenter()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .placeholder(R.drawable.loading)
                                .into(img);
                }
            }
        });
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    count++;
                    if(list.size()== count){
                       dismiss();
                    }else {
                        Glide.with(getActivity())
                                .load(list.get(count))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f)
                                .crossFade()
                                .fitCenter()
                                .skipMemoryCache(true)
                                .animate(R.anim.bounce)
                                .placeholder(R.drawable.loading)
                                .into(img);
                    }
                }
            }
        });
        return root;
    }
}
