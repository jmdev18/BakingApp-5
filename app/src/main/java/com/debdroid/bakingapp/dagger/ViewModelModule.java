package com.debdroid.bakingapp.dagger;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.debdroid.bakingapp.viewmodel.BakingViewModelFactory;
import com.debdroid.bakingapp.viewmodel.RecipeDetailViewModel;
import com.debdroid.bakingapp.viewmodel.RecipeIngredientViewModel;
import com.debdroid.bakingapp.viewmodel.RecipeListViewModel;
import com.debdroid.bakingapp.viewmodel.RecipeStepDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel.class)
    @BakingApplicationScope
    abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    @BakingApplicationScope
    abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel recipeDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeStepDetailViewModel.class)
    @BakingApplicationScope
    abstract ViewModel bindRecipeStepDetailViewModel(RecipeStepDetailViewModel recipeStepDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeIngredientViewModel.class)
    @BakingApplicationScope
    abstract ViewModel bindRecipeIngredientViewModel(RecipeIngredientViewModel recipeIngredientViewModel);

    @Binds
    @BakingApplicationScope
    abstract ViewModelProvider.Factory bindBakingViewModelFactory(BakingViewModelFactory factory);
}
