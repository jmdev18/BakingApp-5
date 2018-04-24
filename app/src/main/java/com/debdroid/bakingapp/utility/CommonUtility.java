package com.debdroid.bakingapp.utility;

import com.debdroid.bakingapp.R;

public class CommonUtility {

    // All images are from free site https://pixabay.com/
    public static int getFallbackImageId(int position) {
        int[] fallbackImages = new int[] {R.drawable.nutella_pie, R.drawable.brownie,
                R.drawable.yellow_cake, R.drawable.cheese_cake};
        return fallbackImages[position];
    }

    public static String capitaliseFirstLetter(String inputString) {
        return inputString.substring(0,1).toUpperCase() + inputString.substring(1);
    }
}
