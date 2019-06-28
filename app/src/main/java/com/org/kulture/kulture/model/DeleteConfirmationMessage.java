package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.org.kulture.kulture.CartActivity;
import com.org.kulture.kulture.R;

/**
 * Created by tkru on 11/15/2017.
 */

public class DeleteConfirmationMessage extends DialogFragment {
    LinearLayout comment_cancels;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MY_DIALOG2);
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
        View root = inflater.inflate(R.layout.delete_confirmation_box, container, false);
        comment_cancels=(LinearLayout)root.findViewById(R.id.comment_cancels);
        comment_cancels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return root;
    }
}