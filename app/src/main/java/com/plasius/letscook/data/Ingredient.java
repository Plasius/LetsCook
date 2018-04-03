package com.plasius.letscook.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long recipeId;
    private String name;
    private int quantity;
    private String measure;



    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Dao
    public interface IngredientDAO{
        @Query("SELECT * FROM ingredients WHERE recipeId=:recipeID")
        List<Ingredient> getByRecipeId(long recipeID);

        @Insert
        void insertAll(Ingredient... ingredients);

        @Query("DELETE FROM ingredients")
        void deleteAll();
    }
}
