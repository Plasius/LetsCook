package com.plasius.letscook.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "ingredients")
public class Ingredient implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    private String name;
    private int quantity;
    private String measure;



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
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

    //Parcelable
    public Ingredient(){

    }

    public Ingredient(Parcel in){
        setId(in.readInt());
        setRecipeId(in.readInt());
        setName(in.readString());
        setQuantity(in.readInt());
        setMeasure(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getRecipeId());
        dest.writeString(getName());
        dest.writeInt(getQuantity());
        dest.writeString(getMeasure());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };



}
