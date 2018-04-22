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
public class StepDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private BakingDatabase testBakingDatabase;

    private RecipeDao testRecipeDao;

    private StepDao testStepDao;

    @Before
    public void createDb() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        testBakingDatabase = Room.inMemoryDatabaseBuilder(context, BakingDatabase.class)
                .allowMainThreadQueries() // For testing purpose only
                .build();

        testRecipeDao = testBakingDatabase.getRecipeDao();
        testStepDao = testBakingDatabase.getStepDao();

        // Insert Recipe to maintain integrity
        testRecipeDao.insertBulkRecipes(TestDatabaseUtil.RECIPE_ENTITY);
    }

    @After
    public void closeDb() throws IOException {
        testBakingDatabase.close();
    }

    @Test
    public void checkRecipeTableIsEmpty() throws InterruptedException {
        List<StepEntity> stepEntityList = LiveDataTestUtil
                .getValue(testStepDao.loadAllStepsAsLiveData());
        assertTrue(stepEntityList.isEmpty());
    }

    @Test
    public void checkSingleInsertAndQuery() throws InterruptedException {
        testStepDao.insertSingleStep(TestDatabaseUtil.STEP_ENTITY_1);
        List<StepEntity> stepEntity = testStepDao.loadAllSteps();
        assertEquals(stepEntity.size(), 1);

        testStepDao.insertSingleStep(TestDatabaseUtil.STEP_ENTITY_2);
        List<StepEntity> stepEntity1 = LiveDataTestUtil
                .getValue(testStepDao.loadAllStepsAsLiveData());
        assertEquals(stepEntity1.size(), 2);
    }

    @Test
    public void checkBulkInsertAndQuery() throws InterruptedException {
        testStepDao.insertBulkSteps(TestDatabaseUtil.STEP_ENTITY);
        List<StepEntity> stepEntity = testStepDao.loadStepsForRecipeId(1);
        assertEquals(stepEntity.size(), 2);

        List<StepEntity> stepEntity1 = LiveDataTestUtil
                .getValue(testStepDao.loadStepsForRecipeIdAsLiveData(2));
        assertEquals(stepEntity1.size(),1);
    }

    @Test
    public void checkDelete() {
        testStepDao.deleteSteps(TestDatabaseUtil.STEP_ENTITY);
        assertEquals(testStepDao.loadAllSteps().size(), 0);
    }
}
