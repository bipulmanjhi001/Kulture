package com.org.kulture.kulture.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.org.kulture.kulture.ProductDetailSub;
import com.org.kulture.kulture.R;

import java.util.ArrayList;

/**
 * Created by tkru on 10/20/2017.
 */

public class ProductDetailSubspinner extends ArrayAdapter<SizeLoading> {

    private Activity activity;
    private ArrayList data;
    SizeLoading tempValues=null;
    LayoutInflater inflater;

    /*************  ProductDetailspinner Constructor *****************/
    public ProductDetailSubspinner(
            ProductDetailSub activitySpinner,
            int textViewResourceId,
            ArrayList objects
    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SizeLoading) data.get(position);
        TextView label = (TextView)row.findViewById(R.id.size);
        if(position==-1){
            // Default selected Spinner item
            label.setText("Choose an option");
        }
        else {
            label.setText(tempValues.getPro_size());
        }
        return row;
    }
}
