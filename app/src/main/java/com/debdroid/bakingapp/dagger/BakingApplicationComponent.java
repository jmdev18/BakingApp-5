package com.debdroid.bakingapp.dagger;

import android.app.Application;

import com.debdroid.bakingapp.BakingApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@BakingApplicationScope
@Component(modules = {
        /* Use AndroidInjectionModule.class if you're not using support library */
        AndroidSupportInjectionModule.class,
        /* The application module */
        BakingApplicationModule.class,
        /* The RecipeListActivity module */
        RecipeListActivityModule.class,
        /* The RecipeDetailActivity module */
        RecipeDetailActivityModule.class,
        /* The RecipeStepDetailActivityModule module */
        RecipeStepDetailActivityModule.class
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
