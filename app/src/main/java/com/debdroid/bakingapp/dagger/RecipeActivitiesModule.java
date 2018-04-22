package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.RecipeDetailActivity;
import com.debdroid.bakingapp.ui.RecipeListActivity;
import com.debdroid.bakingapp.ui.RecipeStepDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = RecipeFragmentsModule.class)
abstract class RecipeActivitiesModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeCustomScope.RecipeListActivityScope
    @ContributesAndroidInjector
    abstract RecipeListActivity contributeRecipeListActivityInjector();

    @RecipeCustomScope.RecipeDetailActivityScope
    @ContributesAndroidInjector
    abstract RecipeDetailActivity contributeRecipeDetailActivityInjector();

    @RecipeCustomScope.RecipeStepDetailActivityScope
    @ContributesAndroidInjector
    abstract RecipeStepDetailActivity contributeRecipeStepDetailActivityInjector();
}
