package com.debdroid.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debdroid.bakingapp.database.RecipeEntity;
import com.debdroid.bakingapp.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeListViewModel extends ViewModel {
    private LiveData<List<RecipeEntity>> recipeEntityList;
    private RecipeRepository recipeRepository;

    @Inject
    public RecipeListViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<List<RecipeEntity>> getRecipes() {
        if(recipeEntityList == null) {
            recipeEntityList = recipeRepository.getRecipes();
        }
        return recipeEntityList;
    }
}
