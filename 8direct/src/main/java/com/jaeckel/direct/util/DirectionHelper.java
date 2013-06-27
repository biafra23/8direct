package com.jaeckel.direct.util;

import android.util.Log;
import com.jaeckel.direct.App;
import com.jaeckel.direct.R;

/**
 * @author flashmop
 * @date 27.06.13 21:23
 */
public class DirectionHelper {

    public static int getDirectionImage(String direction) {
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

    public static int getDirectionIcon(String direction) {

        if ("n".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_n;
        } else if ("e".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_e;
        } else if ("s".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_s;
        } else if ("w".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_w;
        } else if ("ne".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_ne;
        } else if ("sw".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_sw;
        } else if ("nw".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_nw;
        } else if ("se".equalsIgnoreCase(direction)) {

            return R.drawable.ic_stat_se;
        } else {

            return R.drawable.ic_stat_n;
        }
    }

    public static String getLongDirection(String direction) {

        if ("n".equalsIgnoreCase(direction)) {

            return "North";
        } else if ("e".equalsIgnoreCase(direction)) {

            return "East";
        } else if ("s".equalsIgnoreCase(direction)) {

            return "South";
        } else if ("w".equalsIgnoreCase(direction)) {

            return "West";
        } else if ("ne".equalsIgnoreCase(direction)) {

            return "Northeast";
        } else if ("sw".equalsIgnoreCase(direction)) {

            return "Southwest";
        } else if ("nw".equalsIgnoreCase(direction)) {

            return "Northwest";
        } else if ("se".equalsIgnoreCase(direction)) {

            return "Southeast";
        } else {

            return "North";

        }
    }

    public static int directionToInt(String direction) {
        if ("e".equalsIgnoreCase(direction)) {
            return 0;
        } else if ("se".equalsIgnoreCase(direction)) {
            return 1;
        } else if ("s".equalsIgnoreCase(direction)) {
            return 2;
        } else if ("sw".equalsIgnoreCase(direction)) {
            return 3;
        } else if ("w".equalsIgnoreCase(direction)) {
            return 4;
        } else if ("nw".equalsIgnoreCase(direction)) {
            return 5;
        } else if ("n".equalsIgnoreCase(direction)) {
            return 6;

        } else if ("ne".equalsIgnoreCase(direction)) {
            return 7;
        } else {
            Log.e(App.TAG, "Not a valid direction " + direction);
            return -1;
        }
    }
}
