package com.debdroid.bakingapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.debdroid.bakingapp.dagger.DaggerBakingApplicationComponent;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class BakingApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        //Plant Timber tree
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //Create Dagger application component
        DaggerBakingApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this);

        //Initialize Stetho
        Stetho.initializeWithDefaults(this);
    }

    public static BakingApplication get(Context context) {
        return (BakingApplication) context.getApplicationContext();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
