package com.org.kulture.kulture.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.kulture.kulture.R;


/**
 * Created by tkru on 8/26/2017.
 */

public class Terms_of_Sale extends DialogFragment {
    LinearLayout terms_accepted;
    String str;
    TextView header,set_text;
    private static String bodyText;

    public static Terms_of_Sale addSomeString(String temp){
        Terms_of_Sale f = new Terms_of_Sale();
        bodyText = temp;
        return f;
    };
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
        View root = inflater.inflate(R.layout.terms_of_sale, container, false);
        terms_accepted=(LinearLayout)root.findViewById(R.id.terms_accepted);
        set_text=(TextView)root.findViewById(R.id.set_text);
        header=(TextView)root.findViewById(R.id.header);
        if(bodyText.equals("RETURNS ADDRESS")) {
            header.setText(bodyText);
            set_text.setText("Kulture\n" +
                    "\n" +
                    "Mobile\n" +
                    "0428 000 078\n" +
                    "\n" +
                    "Landline\n" +
                    "Within Australia: (07) 4045 4910\n" +
                    "International: +61 7 4045 4910\n" +
                    "\n" +
                     "Email\n"+
                    "admin@kulture.biz\n\n"+
                    "Location\n" +
                    "Cairns, Queensland, Australia");
        }else if(bodyText.equals("ABOUT US")) {
            header.setText(bodyText);
            set_text.setText("Kulture is 100% family owned & operated Indigenous sportswear, workwear & schoolwear label based in Cairns, Queensland, servicing Australia Wide. Proprietors &" +
                    " Graphical artists include, Rose Go Sam – Jiddabul (Ravenshoe) &Kunju " +
                    "(Lockhart River) Aboriginal woman from Far North Queensland.\n" +
                    "Albert Bosen – Thanaquwithi (Old Mapoon) Aboriginal & Wagadagam from Mabuiag Island, " +
                    "Torres Strait Island man. Founded in the year 2008.\n" +
                    "Rose Go Sam Identified a growing interest for an Indigenous clothing label for Australian Indigenous people to feel proud to have a professional business to assist & supply " +
                    "their requirements to look deadly on and off the field.\n" +
                    "Kulture strives on quality, affordability & fast-turn around when it comes to supplying businesses " +
                    "& organisations with deadly designed decorated apparel and promotional products.");
        }
        terms_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str="OK";
                dismiss();
            }
        });
        return root;
    }
}
