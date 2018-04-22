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
    @RecipeCustomScope.BakingApplicationScope
    BakingJsonApiService provideBakingJsonApiService(Retrofit retrofit) {
        return retrofit.create(BakingJsonApiService.class);
    }

    // Retrofit
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(NetworkUtility.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // OkHttp client
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static OkHttpClient provideOkHttpClient(Cache cache, HttpLoggingInterceptor interceptor,
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
    @RecipeCustomScope.BakingApplicationScope
    static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    // Stetho Interceptor
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static StethoInterceptor provideStethoInterceptor() {
        StethoInterceptor stethoInterceptor = new StethoInterceptor();
        return stethoInterceptor;
    }

    // Cache
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static Cache provideCache(File cacheFile) {
        int cacheSize = 10 * 1024 * 1024; //Create a cache of 10 MB
        return new Cache(cacheFile, cacheSize);
    }

    // File
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static File provideFile(Application app) {
        return new File(app.getCacheDir(), "okhttp_cache");
    }

    // Picasso
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static Picasso providePicasso(Application application, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(application)
                //Force Picasso to use the same OkHttp client used by Retrofit
                .downloader(okHttp3Downloader)
                .build();
    }

    // OkHttp downloader
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static OkHttp3Downloader provideOkHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

    // BakingDatabase
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static BakingDatabase provideBakingDatabase(Application app) {
        return Room.databaseBuilder(app, BakingDatabase.class, "BakingDatabase.db")
                .build();
    }

    // RecipeDao
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static RecipeDao provideRecipeDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getRecipeDao();
    }

    // IngredientDao
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static IngredientDao provideIngredientDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getIngredientDao();
    }

    // StepDao
    @Provides
    @RecipeCustomScope.BakingApplicationScope
    static StepDao provideStepDao(BakingDatabase bakingDatabase) {
        return bakingDatabase.getStepDao();
    }
}
