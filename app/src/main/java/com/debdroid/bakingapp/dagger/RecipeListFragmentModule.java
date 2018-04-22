package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeListFragmentModule {
    @ContributesAndroidInjector
    abstract RecipeListFragment contributeRecipeListFragmentInjector();
}
