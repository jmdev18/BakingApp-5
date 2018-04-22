package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeStepDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeStepDetailFragmentModule {
    @ContributesAndroidInjector
    abstract RecipeStepDetailFragment contributeRecipeStepDetailFragmentInjector();
}
