package com.org.kulture.kulture.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.org.kulture.kulture.CategoryListActivity;
import com.org.kulture.kulture.MachineActivity;
import com.org.kulture.kulture.R;

import java.util.ArrayList;


public class MyCustomPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private int i;
    ArrayList<String> bannerimage= new ArrayList<>();

    public MyCustomPagerAdapter(Context context, ArrayList<String> bannerimage) {
        this.context = context;
        this.bannerimage = bannerimage;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
            return bannerimage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //return view == ((View) object);
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.banner_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        for(i=0; i<bannerimage.size(); i++) {
            String geturl=bannerimage.get(position);
            Glide.with(context)
                    .load(geturl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(imageView);
        }
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String check=String.valueOf(position+1);
                if(check.equals("2")) {
                    Intent intent = new Intent(context, CategoryListActivity.class);
                    intent.putExtra("id", "54");
                    intent.putExtra("slug", "kids");
                    intent.putExtra("prename", "Kids");
                    context.startActivity(intent);
                }else if(check.equals("1")) {
                    Intent intent = new Intent(context, MachineActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager) container).removeView((ImageView) object);
        container.removeView((LinearLayout) object);
    }
}