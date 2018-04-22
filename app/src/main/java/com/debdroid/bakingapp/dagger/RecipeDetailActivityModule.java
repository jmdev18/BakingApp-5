package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeDetailActivityModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeDetailActivityScope
    @ContributesAndroidInjector(modules = RecipeDetailFragmentModule.class)
    abstract RecipeDetailActivity contributeRecipeDetailActivityInjector();
}
