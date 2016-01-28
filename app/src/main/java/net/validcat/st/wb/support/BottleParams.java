package net.validcat.st.wb.support;

import net.validcat.st.wb.R;

public class BottleParams {
    public static final int CANCEL_200_ML = 2;
    public static final int CANCEL_400_ML = 3;
    public static final int FULL = R.string.full;
    public static final int EMPTY = R.string.ml0;
    public static int count = 0;
    public static int count_cancel = 1;

    public static int[] bottleImg = {
            R.drawable.ml200,
            R.drawable.ml400,
            R.drawable.ml600,
            R.drawable.ml800,
            R.drawable.ml1000,
            R.drawable.ml1200,
            R.drawable.ml1400,
            R.drawable.ml1600,
            R.drawable.ml1800,
            R.drawable.ml2000
    };

    public static int[] bottleTxt = {
            R.string.ml200,
            R.string.ml400,
            R.string.ml600,
            R.string.ml800,
            R.string.ml1000,
            R.string.ml1200,
            R.string.ml1400,
            R.string.ml1600,
            R.string.ml1800,
            R.string.ml2000
    };

}
