package com.debdroid.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debdroid.bakingapp.database.StepEntity;
import com.debdroid.bakingapp.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class RecipeDetailViewModel extends ViewModel {
    private LiveData<List<StepEntity>> stepEntityList;
    private RecipeRepository recipeRepository;

    @Inject
    public RecipeDetailViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<List<StepEntity>> getSteps(int recipeId) {
        Timber.d("Recipe id"+recipeId);
        // No need to check if stepEntityList is not-null because the recipe id might got changed,
        // sp always fetch data
        stepEntityList = recipeRepository.getSteps(recipeId);
        return stepEntityList;
    }
}
