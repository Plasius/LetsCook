package com.plasius.letscook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.plasius.letscook.MasterActivity;
import com.plasius.letscook.R;
import com.plasius.letscook.data.AppDatabase;
import com.plasius.letscook.utils.PersistenceUtils;
import com.plasius.letscook.utils.StringUtils;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //set up adapter
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent intent = new Intent(context, IngredientRemoteViewService.class);
        views.setRemoteAdapter(R.id.widget_lv_ingredients, intent);

        //set up the title with click
        views.setTextViewText(R.id.widget_tv_title, PersistenceUtils.getSharedPrefString(context, "currentRecipeName", "No recipe found"));
        Intent titleIntent= new Intent(context, MasterActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_tv_title, titlePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(context, RecipeWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}

