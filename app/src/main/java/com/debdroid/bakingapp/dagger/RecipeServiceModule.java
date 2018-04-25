package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.widget.BakingAppWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeServiceModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeCustomScope.RecipeServiceScope
    @ContributesAndroidInjector
    abstract BakingAppWidgetService contributeBakingAppWidgetServiceInjector();
}
