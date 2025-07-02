package com.example.homework_08;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MyWidgetConfigureActivity MyWidgetConfigureActivity}
 */
public class MyWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = MyWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        CharSequence captionText = MyWidgetConfigureActivity.loadCaptionPref(context, appWidgetId);
        if (captionText.length() == 0) {
            captionText = widgetText;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(widgetText.toString()));
        PendingIntent pending= PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        views.setOnClickPendingIntent(R.id.appwidget_text, pending);
        views.setTextViewText(R.id.appwidget_text, captionText);
        views.setViewVisibility(R.id.Name, View.VISIBLE);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}