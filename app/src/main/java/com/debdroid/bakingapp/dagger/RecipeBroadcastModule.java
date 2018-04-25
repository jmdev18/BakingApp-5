package com.debdroid.bakingapp.dagger;

import com.debdroid.bakingapp.ui.widget.BakingAppWidgetProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeBroadcastModule {
    // This is the shortcut way, refer https://google.github.io/dagger/android
    @RecipeCustomScope.RecipeBroadcastReceiverScope
    @ContributesAndroidInjector
    abstract BakingAppWidgetProvider contributeBakingAppWidgetProviderInjector();
}
