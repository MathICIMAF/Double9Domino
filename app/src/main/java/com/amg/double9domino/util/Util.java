package com.amg.double9domino.util;

//import android.support.v7.internal.widget.ActivityChooserView;

import androidx.customview.widget.ExploreByTouchHelper;

public class Util {
    public static int max(int... totals) {
        int max = ExploreByTouchHelper.INVALID_ID;
        for (int i = 0; i < totals.length; i++) {
            if (totals[i] > max) {
                max = totals[i];
            }
        }
        return max;
    }

    public static int min(int... values) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return min;
    }
}
