package com.plasius.letscook.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = false)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "servings")
    private int servings;

    @ColumnInfo(name = "imageURL")
    private String imageURL;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Dao
    public interface RecipeDAO{
        @Query("SELECT * FROM recipes")
        List<Recipe> getAll();

        @Query("SELECT * FROM recipes WHERE id= :id")
        Recipe getRecipeById(long id);

        @Query("SELECT COUNT(*) from recipes")
        int getCount();

        @Insert
        void insertAll(Recipe... recipes);

        @Query("DELETE FROM recipes")
        void deleteAll();
    }
}


