package com.debdroid.bakingapp.utility;

import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.database.RecipeEntity;
import com.debdroid.bakingapp.database.StepEntity;
import com.debdroid.bakingapp.datamodel.Ingredient;
import com.debdroid.bakingapp.datamodel.Recipe;
import com.debdroid.bakingapp.datamodel.Step;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtility {

    /**
     * Utility method to parse Recipe json and populate the data for database entry
     * @param recipeList Json recipe list
     * @return Recipe list data for database entry
     */
    public static List<RecipeEntity> getPriceEntityList(List<Recipe> recipeList) {

        List<RecipeEntity> recipeEntityList = new ArrayList<>();

        for(Recipe recipe : recipeList) {
            RecipeEntity recipeEntity = new RecipeEntity();
            recipeEntity.id = recipe.getId();
            recipeEntity.name = recipe.getName();
            recipeEntity.servings = recipe.getServings();
            recipeEntity.image = recipe.getImage();
            recipeEntityList.add(recipeEntity);
        }

        return recipeEntityList;
    }

    /**
     * Utility method to parse Recipe json and populate the data for database entry
     * @param recipeList Json recipe list
     * @return Ingredient list data for database entry
     */
    public static List<IngredientEntity> getIngredientEntityList(List<Recipe> recipeList) {

        List<IngredientEntity> ingredientEntityList = new ArrayList<>();

        for(Recipe recipe : recipeList) {
            List<Ingredient> ingredientList = recipe.getIngredients();
            for(Ingredient ingredient : ingredientList) {
                IngredientEntity ingredientEntity = new IngredientEntity();
                ingredientEntity.recipe_id = recipe.getId();
                ingredientEntity.quantity = ingredient.getQuantity();
                ingredientEntity.measure = ingredient.getMeasure();
                ingredientEntity.ingredient = ingredient.getIngredient();
                ingredientEntityList.add(ingredientEntity);
            }
        }

        return ingredientEntityList;
    }

    /**
     * Utility method to parse Recipe json and populate the data for database entry
     * @param recipeList Json recipe list
     * @return Step list data for database entry
     */
    public static List<StepEntity> getStepEntityList(List<Recipe> recipeList) {

        List<StepEntity> stepEntityList = new ArrayList<>();

        for(Recipe recipe : recipeList) {
            List<Step> stepList = recipe.getSteps();
            for(Step step : stepList) {
                StepEntity stepEntity = new StepEntity();
                stepEntity.recipe_id = recipe.getId();
                stepEntity.stepId = step.getId();
                stepEntity.shortDescription = step.getShortDescription();
                stepEntity.description = step.getDescription();
                stepEntity.videoURL = step.getVideoURL();
                stepEntity.thumbnailURL = step.getThumbnailURL();
                stepEntityList.add(stepEntity);
            }
        }

        return stepEntityList;
    }
}
