package com.debdroid.bakingapp.viewmodel;

import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeWidgetViewModel {
    private List<IngredientEntity> ingredientEntityList;
    private RecipeRepository recipeRepository;

    @Inject
    public RecipeWidgetViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<IngredientEntity> getIngredientsAsList(int recipeId) {
        ingredientEntityList = recipeRepository.getIngredientsAsList(recipeId);
        return ingredientEntityList;
    }
}
