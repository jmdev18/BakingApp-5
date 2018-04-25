package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.widget.RecipeAppWidgetProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeBroadcastModule {
    @RecipeCustomScope.RecipeBroadcastReceiverScope
    @ContributesAndroidInjector
    abstract RecipeAppWidgetProvider contributeRecipeAppWidgetProviderInjector();
}
