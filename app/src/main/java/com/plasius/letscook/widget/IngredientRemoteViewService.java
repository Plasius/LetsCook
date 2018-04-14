package com.plasius.letscook.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.plasius.letscook.adapters.WidgetAdapter;

public class IngredientRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(this.getApplicationContext(), intent);
    }
}
