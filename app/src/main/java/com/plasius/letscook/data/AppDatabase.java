package com.plasius.letscook.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract Recipe.RecipeDAO recipeDAO();
    public abstract Ingredient.IngredientDAO ingredientDAO();
    public abstract Step.StepDAO stepDAO();

    public static AppDatabase getAppDatabase(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "letscook.db").build();

        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
