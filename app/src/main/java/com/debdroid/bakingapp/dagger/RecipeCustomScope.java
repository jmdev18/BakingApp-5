package com.debdroid.bakingapp.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface RecipeCustomScope {
    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface BakingApplicationScope {
    }

    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface RecipeListActivityScope {
    }

    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface RecipeDetailActivityScope {
    }

    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface RecipeStepDetailActivityScope {
    }

    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface RecipeBroadcastReceiverScope {
    }

    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface RecipeServiceScope {
    }
}
