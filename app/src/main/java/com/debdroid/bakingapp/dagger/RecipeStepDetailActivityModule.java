package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeStepDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeStepDetailActivityModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeStepDetailActivityScope
    @ContributesAndroidInjector(modules = {RecipeStepDetailFragmentModule.class, RecipeIngredientFragmentModule.class})
    abstract RecipeStepDetailActivity contributeRecipeStepDetailActivityInjector();
}
