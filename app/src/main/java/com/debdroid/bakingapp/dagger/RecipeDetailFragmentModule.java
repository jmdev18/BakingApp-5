package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeDetailFragmentModule {
    @ContributesAndroidInjector
    abstract RecipeDetailFragment contributeRecipeDetailFragmentInjector();
}
