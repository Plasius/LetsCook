package com.plasius.letscook.adapters;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.plasius.letscook.R;
import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.data.Ingredient;
import com.plasius.letscook.utils.PersistenceUtils;

import java.util.List;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    List<Ingredient> ingredientList;

    public WidgetAdapter(Context context, Intent intent){
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredientList = AppDatabase.getAppDatabase(context).ingredientDAO()
                .getByRecipeId(PersistenceUtils.getSharedPrefInt(context,"currentRecipe", 1));

    }

    @Override
    public void onDestroy() {
        ingredientList = null;
        context = null;
    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        rv.setTextViewText(R.id.widget_item_tv, ingredientList.get(position).getQuantity() + " "
                + ingredientList.get(position).getMeasure() + " of "
                + ingredientList.get(position).getName());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return ingredientList.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
