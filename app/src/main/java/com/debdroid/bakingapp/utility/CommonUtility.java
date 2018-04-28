package com.debdroid.bakingapp.utility;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.database.IngredientEntity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CommonUtility {

    /**
     * This method chooses the correct fallback image for the recipe
     * @param position the position of the card (i.e. adapter)
     * @return int id of the corresponding fallback image
     */
    // All images are from free site https://pixabay.com/
    public static int getFallbackImageId(int position) {
        int[] fallbackImages = new int[] {R.drawable.nutella_pie, R.drawable.brownie,
                R.drawable.yellow_cake, R.drawable.cheese_cake};
        return fallbackImages[position];
    }

    /**
     * This method formats the double number to friendly display
     * @param input The double numbr to be formatted
     * @return Friendly string number
     */
    public static String formatNumber(double input) {
        NumberFormat numberFormat = new DecimalFormat("0.#");
        return numberFormat.format(input);
    }

    /**
     * This method capitalise the first letter of the input String
     * @param inputString The input string
     * @return The string with first letter of the first word capitalized
     */
    public static String capitaliseFirstLetter(String inputString) {
        return inputString.substring(0,1).toUpperCase() + inputString.substring(1);
    }

    /**
     * This method concatenate the Ingredients to a single string using String Builder
     * @param ingredientEntityList The ingredients list
     * @return Concatenated ingredient list in a single string
     */
    public static String createFriendlyIngredientList(List<IngredientEntity> ingredientEntityList) {
        StringBuilder ingredientList = new StringBuilder();
        int count = 1;
        for(IngredientEntity ingredientEntity : ingredientEntityList) {
            StringBuilder singleIngredient = new StringBuilder();
            singleIngredient.append(count);
            singleIngredient.append(") ");
            singleIngredient.append(capitaliseFirstLetter(ingredientEntity.ingredient));
            singleIngredient.append(" - ");
            singleIngredient.append(formatNumber(ingredientEntity.quantity));
            singleIngredient.append(" ");
            singleIngredient.append(ingredientEntity.measure);
            singleIngredient.append("\n");
            ingredientList.append(singleIngredient.toString());
            count++;
        }
        return ingredientList.toString();
    }
}
