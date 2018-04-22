package com.debdroid.bakingapp.database;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
public class IngredientDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private BakingDatabase testBakingDatabase;

    private RecipeDao testRecipeDao;

    private IngredientDao testIngredientDao;

    @Before
    public void createDb() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        testBakingDatabase = Room.inMemoryDatabaseBuilder(context, BakingDatabase.class)
                .allowMainThreadQueries() // For testing purpose only
                .build();

        testRecipeDao = testBakingDatabase.getRecipeDao();
        testIngredientDao = testBakingDatabase.getIngredientDao();

        // Insert Recipe to maintain integrity
        testRecipeDao.insertBulkRecipes(TestDatabaseUtil.RECIPE_ENTITY);
    }

    @After
    public void closeDb() throws IOException {
        testBakingDatabase.close();
    }

    @Test
    public void checkRecipeTableIsEmpty() throws InterruptedException {
        List<IngredientEntity> ingredientEntityList = LiveDataTestUtil
                .getValue(testIngredientDao.loadAllIngredientsAsLiveData());
        assertTrue(ingredientEntityList.isEmpty());
    }

    @Test
    public void checkSingleInsertAndQuery() throws InterruptedException {
        testIngredientDao.insertSingleIngredient(TestDatabaseUtil.INGREDIENT_ENTITY_1);
        List<IngredientEntity> ingredientEntity = testIngredientDao.loadAllIngredients();
        assertEquals(ingredientEntity.size(), 1);

        testIngredientDao.insertSingleIngredient(TestDatabaseUtil.INGREDIENT_ENTITY_2);
        List<IngredientEntity> ingredientEntity1 = LiveDataTestUtil
                .getValue(testIngredientDao.loadAllIngredientsAsLiveData());
        assertEquals(ingredientEntity1.size(), 2);
    }

    @Test
    public void checkBulkInsertAndQuery() throws InterruptedException {
        testIngredientDao.insertBulkIngredients(TestDatabaseUtil.INGREDIENT_ENTITY);
        List<IngredientEntity> ingredientEntity = testIngredientDao.loadIngredientsForRecipeId(1);
        assertEquals(ingredientEntity.size(), 2);

        List<IngredientEntity> ingredientEntity1 = LiveDataTestUtil
                .getValue(testIngredientDao.loadIngredientsForRecipeIdAsLiveData(2));
        assertEquals(ingredientEntity1.size(),1);
    }

    @Test
    public void checkDelete() {
        testIngredientDao.deleteIngredients(TestDatabaseUtil.INGREDIENT_ENTITY);
        assertEquals(testIngredientDao.loadAllIngredients().size(), 0);
    }
}
