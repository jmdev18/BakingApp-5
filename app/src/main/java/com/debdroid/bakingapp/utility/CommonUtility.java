package com.debdroid.bakingapp.utility;

import com.debdroid.bakingapp.R;
import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.datamodel.Ingredient;

import java.util.List;

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

    public static String createFriendlyIngredientList(List<IngredientEntity> ingredientEntityList) {
        StringBuilder ingredientList = new StringBuilder();
        for(IngredientEntity ingredientEntity : ingredientEntityList) {
            StringBuilder singleIngredient = new StringBuilder();
            singleIngredient.append(capitaliseFirstLetter(ingredientEntity.ingredient));
            singleIngredient.append(" - ");
            singleIngredient.append(ingredientEntity.quantity);
            singleIngredient.append(" ");
            singleIngredient.append(ingredientEntity.measure);
            singleIngredient.append("\n");
            ingredientList.append(singleIngredient);
        }
        return ingredientList.toString();
    }
}
