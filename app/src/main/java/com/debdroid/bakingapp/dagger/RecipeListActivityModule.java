package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeListActivityModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeListActivityScope
    @ContributesAndroidInjector (modules = RecipeListFragmentModule.class)
    abstract RecipeListActivity contributeRecipeListActivityInjector();
}
