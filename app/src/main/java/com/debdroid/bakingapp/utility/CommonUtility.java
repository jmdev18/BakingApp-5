package com.debdroid.bakingapp.utility;

import com.debdroid.bakingapp.R;

public class CommonUtility {

    public static int getFallbackImageId(int position) {
        int[] fallbackImages = new int[] {R.drawable.nutella_pie, R.drawable.brownie,
                R.drawable.yellow_cake, R.drawable.cheesecake};
        return fallbackImages[position];
    }
}
