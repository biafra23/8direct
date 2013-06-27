package com.jaeckel.direct.fragments;

import android.app.Activity;
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
import com.jaeckel.direct.DirectionHolder;
import com.jaeckel.direct.R;

/**
 * Created by quirijngb on 12/06/2013.
 */
public class DirectionFragment extends Fragment
{
   public static final String ARG_SECTION_DIRECTION = "section_code";
   public static final String ARG_SECTION_DIRECTION_LONG = "section_description";
   private DirectionHolder directionListener;
   private String direction;
   private String longDirection;

   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      directionListener = (DirectionHolder) activity;
   }

   static public Fragment newInstance(String direction, String description)
   {
      DirectionFragment f = new DirectionFragment();
      Bundle args = new Bundle();
      args.putString(DirectionFragment.ARG_SECTION_DIRECTION, direction);
      args.putString(DirectionFragment.ARG_SECTION_DIRECTION_LONG, description);
      f.setArguments(args);

      return f;
   }

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      Bundle args = getArguments();
      direction = args.getString(ARG_SECTION_DIRECTION);
      longDirection = args.getString(ARG_SECTION_DIRECTION_LONG);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {

      View rootView = inflater.inflate(R.layout.fragment_direction, container, false);

      Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

      Log.d(App.TAG, "String: " + direction);

      TextView mShortDirection = (TextView) rootView.findViewById(R.id.direction_short);
      TextView mLongDirection = (TextView) rootView.findViewById(R.id.direction_description);
      ImageView mPortals = (ImageView) rootView.findViewById(R.id.portals);
      Button mActivateButton = (Button) rootView.findViewById(R.id.activate);
      Button mDeactivateButton = (Button) rootView.findViewById(R.id.deactivate);

      mShortDirection.setTypeface(tf);
      mShortDirection.setText(direction);

      mLongDirection.setText(longDirection);

      mPortals.setImageResource(getDirectionImage(direction));

      mActivateButton.setOnClickListener(new View.OnClickListener()
         {
            public void onClick(View view)
            {
               setActivated(true);
            }
         });
      mDeactivateButton.setOnClickListener(new View.OnClickListener()
         {
            public void onClick(View view)
            {
               setActivated(false);
            }
         });
      return rootView;
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState)
   {
      super.onViewCreated(view, savedInstanceState);
      updateView();
   }

   public void setActivated(boolean activated)
   {
      directionListener.setActivated(direction, activated);
      updateView();
   }

   public void notifyDataSetChanged()
   {
      updateView();
   }

   private void updateView()
   {
      View rootView = getView();
      ImageView mPortals = (ImageView) rootView.findViewById(R.id.portals);
      TextView mShortDirection = (TextView) rootView.findViewById(R.id.direction_short);
      TextView mLongDirection = (TextView) rootView.findViewById(R.id.direction_description);
      Button mActivateButton = (Button) rootView.findViewById(R.id.activate);
      Button mDeactivateButton = (Button) rootView.findViewById(R.id.deactivate);
      View mContainer = rootView.findViewById(R.id.container);
      if (directionListener.isActivated(direction))
      {
         mPortals.setVisibility(View.VISIBLE);
         mLongDirection.setTextColor(getActivity().getResources().getColor(R.color.text_highlighted));
         mShortDirection.setTextColor(getActivity().getResources().getColor(R.color.text_highlighted));
         mContainer.setBackgroundResource(R.drawable.draw_bgbox_gradient);

         if (directionListener.isAssignedToMe(direction))
         {
            mDeactivateButton.setVisibility(View.VISIBLE);
         }
         else
         {
            mDeactivateButton.setVisibility(View.INVISIBLE);
         }
         mActivateButton.setVisibility(View.GONE);
      }
      else
      {
         mPortals.setVisibility(View.INVISIBLE);
         mLongDirection.setTextColor(getActivity().getResources().getColor(R.color.text));
         mShortDirection.setTextColor(getActivity().getResources().getColor(R.color.text));
         mContainer.setBackgroundResource(R.drawable.draw_bgbox_black);
         if (directionListener.isAssignedToMe(direction))
         {
            mActivateButton.setVisibility(View.VISIBLE);
         }
         else
         {
            mActivateButton.setVisibility(View.INVISIBLE);
         }
         mDeactivateButton.setVisibility(View.GONE);
      }
      if (directionListener.isAssignedToMe(direction))
      {
         getView().setAlpha(1.0F);
      }
      else
      {
         getView().setAlpha(0.5F);
      }
   }

   private int getDirectionImage(String direction)
   {
      if ("n".equalsIgnoreCase(direction))
      {

         return R.drawable.portal_n;

      }
      else if ("e".equalsIgnoreCase(direction))
      {

         return R.drawable.portal_e;

      }
      else if ("s".equalsIgnoreCase(direction))
      {

         return R.drawable.portal_s;

      }
      else if ("w".equalsIgnoreCase(direction))
      {

         return R.drawable.portal_w;

      }
      else if ("ne".equalsIgnoreCase(direction))
      {
         return R.drawable.portal_ne;

      }
      else if ("sw".equalsIgnoreCase(direction))
      {
         return R.drawable.portal_sw;

      }
      else if ("nw".equalsIgnoreCase(direction))
      {
         return R.drawable.portal_nw;

      }
      else if ("se".equalsIgnoreCase(direction))
      {
         return R.drawable.portal_se;

      }
      else
      {
         return R.drawable.portal_n;

      }
   }
}
