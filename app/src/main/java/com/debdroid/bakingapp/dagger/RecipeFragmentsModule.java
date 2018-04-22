package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeDetailFragment;
import com.debdroid.bakingapp.ui.RecipeIngredientFragment;
import com.debdroid.bakingapp.ui.RecipeListFragment;
import com.debdroid.bakingapp.ui.RecipeStepDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeFragmentsModule {
    @RecipeCustomScope.RecipeListActivityScope
    @ContributesAndroidInjector
    abstract RecipeListFragment contributeRecipeListFragmentInjector();

    @RecipeCustomScope.RecipeDetailActivityScope
    @ContributesAndroidInjector
    abstract RecipeDetailFragment contributeRecipeDetailFragmentInjector();

    @RecipeCustomScope.RecipeStepDetailActivityScope
    @ContributesAndroidInjector
    abstract RecipeStepDetailFragment contributeRecipeStepDetailFragmentInjector();

    @RecipeCustomScope.RecipeStepDetailActivityScope
    @ContributesAndroidInjector
    abstract RecipeIngredientFragment contributeRecipeIngredientFragmentInjector();
}
