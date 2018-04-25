package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.widget.RecipeAppWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeServiceModule {
    @RecipeCustomScope.RecipeServiceScope
    @ContributesAndroidInjector
    abstract RecipeAppWidgetService contributeRecipeAppWidgetServiceInjector();
}
