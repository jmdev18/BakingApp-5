package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeIngredientFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeIngredientFragmentModule {
    @ContributesAndroidInjector
    abstract RecipeIngredientFragment contributeRecipeIngredientFragmentInjector();
}