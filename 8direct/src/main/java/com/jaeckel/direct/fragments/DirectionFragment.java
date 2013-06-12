package com.jaeckel.direct.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private boolean activated = false;
    private ImageView mPortals;
    private TextView mLongDirection;
    private TextView mShortDirection;
    private View mContainer;
    private Button mActivateButton;
    private Button mDeactivateButton;


    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_direction, container, false);
        Bundle args = getArguments();


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");


        final String string = args.getString(ARG_SECTION_DIRECTION);
        Log.d(App.TAG, "String: " + string);

        mContainer = rootView.findViewById(R.id.container);

        mShortDirection = (TextView) rootView.findViewById(R.id.direction_short);
        mShortDirection.setTypeface(tf);
        mShortDirection.setText(string);

        mLongDirection = (TextView) rootView.findViewById(R.id.direction_description);
        mLongDirection.setText(args.getString(ARG_SECTION_DIRECTION_LONG));

        mPortals = (ImageView) rootView.findViewById(R.id.portals);
        if ("sw".equalsIgnoreCase(string)) {
            mPortals.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(App.TAG, "sw clicked");
                }
            });
        }
        mPortals.setImageResource(getDirectionImage(string));

        mActivateButton = (Button) rootView.findViewById(R.id.activate);
        mActivateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setActivated(true);
            }
        });
        mDeactivateButton = (Button) rootView.findViewById(R.id.deactivate);
        mDeactivateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setActivated(false);
            }
        });
        return rootView;
    }

    private void updateView() {
        if (activated) {
            mPortals.setVisibility(View.VISIBLE);
            mLongDirection.setTextColor(getActivity().getResources().getColor(R.color.text_highlighted));
            mShortDirection.setTextColor(getActivity().getResources().getColor(R.color.text_highlighted));
            mContainer.setBackgroundResource(R.drawable.draw_bgbox_gradient);
            mDeactivateButton.setVisibility(View.VISIBLE);
            mActivateButton.setVisibility(View.GONE);
        } else {
            mPortals.setVisibility(View.GONE);
            mLongDirection.setTextColor(getActivity().getResources().getColor(R.color.text));
            mShortDirection.setTextColor(getActivity().getResources().getColor(R.color.text));
            mContainer.setBackgroundColor(getActivity().getResources().getColor(R.color.card_background));
            mActivateButton.setVisibility(View.VISIBLE);
            mDeactivateButton.setVisibility(View.GONE);
        }
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
