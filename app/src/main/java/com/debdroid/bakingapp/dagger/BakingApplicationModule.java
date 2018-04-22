package com.debdroid.bakingapp.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.debdroid.bakingapp.database.BakingDatabase;
import com.debdroid.bakingapp.database.IngredientDao;
import com.debdroid.bakingapp.database.RecipeDao;
import com.debdroid.bakingapp.database.StepDao;
import com.debdroid.bakingapp.retrofit.BakingJsonApiService;
import com.debdroid.bakingapp.utility.NetworkUtility;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module(includes = ViewModelModule.class)
class BakingApplicationModule {

    // Baking Json data api service using Retrofit
    @Provides
    @BakingApplicationScope
    BakingJsonApiService provideBakingJsonApiService(Retrofit retrofit) {
        return retrofit.create(BakingJsonApiService.class);
    }

    // Retrofit
    @Provides
    @BakingApplicationScope
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(NetworkUtility.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // OkHttp client
    @Provides
    @BakingApplicationScope
    OkHttpClient provideOkHttpClient(Cache cache, HttpLoggingInterceptor interceptor,
                                     StethoInterceptor stethoInterceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(stethoInterceptor)
                .build();
    }

    // OkHttp logging Interceptor
    @Provides
    @BakingApplicationScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    // Stetho Interceptor
    @Provides
    @BakingApplicationScope
    StethoInterceptor provideStethoInterceptor() {
        StethoInterceptor stethoInterceptor = new StethoInterceptor();
        return stethoInterceptor;
    }

    // Cache
    @Provides
    @BakingApplicationScope
    Cache provideCache(File cacheFile) {
        int cacheSize = 10 * 1024 * 1024; //Create a cache of 10 MB
        return new Cache(cacheFile, cacheSize);
    }

    // File
    @Provides
    @BakingApplicationScope
    File provideFile(Application app) {
        return new File(app.getCacheDir(), "okhttp_cache");
    }

    // Picasso
    @Provides
    @BakingApplicationScope
    Picasso providePicasso(Application application, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(application)
                //Force Picasso to use the same OkHttp client used by Retrofit
                .downloader(okHttp3Downloader)
                .build();
    }

    // OkHttp downloader
    @Provides
    @BakingApplicationScope
    OkHttp3Downloader provideOkHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

    // BakingDatabase
    @Provides
    @BakingApplicationScope
    BakingDatabase provideBakingDatabase(Application app) {
        return Room.databaseBuilder(app, BakingDatabase.class, "BakingDatabase.db")
                .build();
    }

    // RecipeDao
    @Provides
    @BakingApplicationScope
    RecipeDao provideRecipeDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getRecipeDao();
    }

    // IngredientDao
    @Provides
    @BakingApplicationScope
    IngredientDao provideIngredientDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getIngredientDao();
    }

    // StepDao
    @Provides
    @BakingApplicationScope
    StepDao provideStepDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getStepDao();
    }
}
