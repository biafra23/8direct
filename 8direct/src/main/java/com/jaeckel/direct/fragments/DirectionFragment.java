package com.jaeckel.direct.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeckel.direct.App;
import com.jaeckel.direct.R;

/**
 * Created by quirijngb on 12/06/2013.
 */
public class DirectionFragment extends Fragment {
    public static final String ARG_SECTION_DIRECTION = "section_code";
    public static final String ARG_SECTION_DIRECTION_LONG = "section_description";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_direction, container, false);
        Bundle args = getArguments();
        final String string = args.getString(ARG_SECTION_DIRECTION);
        Log.d(App.TAG, "String: " + string);
        ((TextView) rootView.findViewById(R.id.direction_short)).setText(string);

        ((TextView) rootView.findViewById(R.id.direction_description)).setText(args.getString(ARG_SECTION_DIRECTION_LONG));

        ImageView portals = (ImageView) rootView.findViewById(R.id.portals);
        if ("sw".equalsIgnoreCase(string)) {
            portals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(App.TAG, "sw clicked");
                }
            });
        }
        portals.setImageResource(getDirectionImage(string));
        return rootView;
    }

    private int getDirectionImage(String direction) {
        if ("n".equalsIgnoreCase(direction)) {

            return R.drawable.portal_n;

        } else if ("e".equalsIgnoreCase(direction)) {

            return R.drawable.portal_e;

        } else if ("s".equalsIgnoreCase(direction)) {

            return R.drawable.portal_s;

        } else if ("w".equalsIgnoreCase(direction)) {

            return R.drawable.portal_w;

        } else if ("ne".equalsIgnoreCase(direction)) {
            return R.drawable.portal_ne;

        } else if ("sw".equalsIgnoreCase(direction)) {
            return R.drawable.portal_sw;

        } else if ("nw".equalsIgnoreCase(direction)) {
            return R.drawable.portal_nw;

        } else if ("se".equalsIgnoreCase(direction)) {
            return R.drawable.portal_se;

        } else {
            return R.drawable.portal_n;

        }
    }
}
