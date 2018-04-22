package com.debdroid.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debdroid.bakingapp.database.StepEntity;
import com.debdroid.bakingapp.repository.RecipeRepository;

import javax.inject.Inject;

public class RecipeStepDetailViewModel extends ViewModel {
    private LiveData<StepEntity> stepEntity;
    private RecipeRepository recipeRepository;

    @Inject
    public RecipeStepDetailViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<StepEntity> getStep(int recipeId, int stepId) {
        stepEntity = recipeRepository.getStep(recipeId, stepId);
        return stepEntity;
    }
}
