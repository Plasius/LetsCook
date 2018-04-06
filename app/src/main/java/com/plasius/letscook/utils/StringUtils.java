package com.plasius.letscook.utils;

import com.plasius.letscook.data.Ingredient;

import java.util.List;

public class StringUtils {
    public static String getStringFromIngredientList(List<Ingredient> ingredientList){
        StringBuilder stringBuilder = new StringBuilder(ingredientList.size());

        for(Ingredient ingredient : ingredientList){
            stringBuilder.append(ingredient.getQuantity());
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getMeasure());
            stringBuilder.append(" of ");
            stringBuilder.append(ingredient.getName());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();

    }
}
