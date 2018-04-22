package com.debdroid.bakingapp.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.debdroid.bakingapp.util.LiveDataTestUtil;
import com.debdroid.bakingapp.util.TestDatabaseUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private BakingDatabase testBakingDatabase;

    private RecipeDao testRecipeDao;

    @Before
    public void createDb() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        testBakingDatabase = Room.inMemoryDatabaseBuilder(context, BakingDatabase.class)
                .allowMainThreadQueries() // For testing purpose only
                .build();

        testRecipeDao = testBakingDatabase.getRecipeDao();
    }

    @After
    public void closeDb() throws IOException {
        testBakingDatabase.close();
    }

    @Test
    public void checkRecipeTableIsEmpty() throws InterruptedException {
        List<RecipeEntity> recipeEntityList = LiveDataTestUtil
                .getValue(testRecipeDao.loadAllRecipesAsLiveData());
        assertTrue(recipeEntityList.isEmpty());
    }

    @Test
    public void checkSingleInsertAndQuery() throws InterruptedException {
        testRecipeDao.insertSingleRecipe(TestDatabaseUtil.RECIPE_ENTITY_1);
        RecipeEntity recipeEntity = testRecipeDao.loadSingleRecipe(1);
        assertEquals(recipeEntity.id, 1);

        RecipeEntity recipeEntity1 = LiveDataTestUtil
                .getValue(testRecipeDao.loadSingleRecipeAsLiveData(1));
        assertEquals(recipeEntity1.name, "recipe 1");
    }

    @Test
    public void checkBulkInsertAndQuery() throws InterruptedException {
        testRecipeDao.insertBulkRecipes(TestDatabaseUtil.RECIPE_ENTITY);
        List<RecipeEntity> recipeEntityList = testRecipeDao.loadAllRecipes();
        assertEquals(recipeEntityList.size(),2);

        List<RecipeEntity> recipeEntityList1 = LiveDataTestUtil
                .getValue(testRecipeDao.loadAllRecipesAsLiveData());
        assertEquals(recipeEntityList1.size(),2);
    }

    @Test
    public void checkDelete() {
        testRecipeDao.deleteRecipes(TestDatabaseUtil.RECIPE_ENTITY);
        assertEquals(testRecipeDao.loadAllRecipes().size(), 0);
    }
}
