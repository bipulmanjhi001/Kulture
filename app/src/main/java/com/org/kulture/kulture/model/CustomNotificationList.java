package com.org.kulture.kulture.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.kulture.kulture.R;

/**
 * Created by tkru on 9/12/2017.
 */

public class CustomNotificationList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomNotificationList(Activity context,
                       String[] web, Integer[] imageId) {
        super(context, R.layout.notification_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.notification_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.notification_title);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.notification_list_image);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}