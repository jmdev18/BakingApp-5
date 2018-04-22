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
    @RecipeCustomScope.BakingApplicationScope
    abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailViewModel.class)
    @RecipeCustomScope.BakingApplicationScope
    abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel recipeDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeStepDetailViewModel.class)
    @RecipeCustomScope.BakingApplicationScope
    abstract ViewModel bindRecipeStepDetailViewModel(RecipeStepDetailViewModel recipeStepDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeIngredientViewModel.class)
    @RecipeCustomScope.BakingApplicationScope
    abstract ViewModel bindRecipeIngredientViewModel(RecipeIngredientViewModel recipeIngredientViewModel);

    @Binds
    @RecipeCustomScope.BakingApplicationScope
    abstract ViewModelProvider.Factory bindBakingViewModelFactory(BakingViewModelFactory factory);
}
