package com.debdroid.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debdroid.bakingapp.database.IngredientEntity;
import com.debdroid.bakingapp.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeIngredientViewModel extends ViewModel {
    private LiveData<List<IngredientEntity>> ingredientEntityList;
    private RecipeRepository recipeRepository;

    @Inject
    public RecipeIngredientViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<List<IngredientEntity>> getIngredients(int recipeId) {
        ingredientEntityList = recipeRepository.getIngredients(recipeId);
        return ingredientEntityList;
    }
}
