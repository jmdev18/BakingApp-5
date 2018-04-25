package com.debdroid.bakingapp.dagger;

import android.app.Application;

import com.debdroid.bakingapp.BakingApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@RecipeCustomScope.BakingApplicationScope
@Component(modules = {
        /* Use AndroidInjectionModule.class if you're not using support library */
        AndroidSupportInjectionModule.class,
        /* The application module */
        BakingApplicationModule.class,
        /* The module for all the activities */
        RecipeActivitiesModule.class,
        /* The module for widget provider which is a broadcast receiver */
        RecipeBroadcastModule.class,
        /* The module for service used for widget */
        RecipeServiceModule.class
        })
public interface BakingApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        BakingApplicationComponent build();
    }

    void inject(BakingApplication app);
}
