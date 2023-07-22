package com.codify92.reminderappmaterialdesign.ForgroundService;


import static com.codify92.reminderappmaterialdesign.App.CHANNEL_ID;
import static com.codify92.reminderappmaterialdesign.BroadcastReceiver.NextListenerReceiver.i;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.codify92.reminderappmaterialdesign.BroadcastReceiver.NextListenerReceiver;
import com.codify92.reminderappmaterialdesign.BroadcastReceiver.PreviousListenerReceiver;
import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;


import java.util.ArrayList;

public class TodoListService extends Service {

    public static Notification notification;

    public static RemoteViews collapsedViews;
    public static RemoteViews expandedViews;

    //TODO:DATABASE
    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;

    public static ArrayList<TodoModelClass> todoArrayList;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        todoArrayList = new ArrayList<>();
        dbHelper = new SQLiteDatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        //TODO: REMOTE VIEWS
        collapsedViews = new RemoteViews(getPackageName(), R.layout.custom_notification_collapsed);
        expandedViews = new RemoteViews(getPackageName(),R.layout.custom_notification_expanded);

        collapsedViews.setTextViewText(R.id.notification_collapsed_title,intent.getStringExtra("title"));
        expandedViews.setTextViewText(R.id.notification_expanded_title,intent.getStringExtra("title"));

        //TODO:PENDING INTENT FOR CLICK LISTENER FOR EXPANDED VIEWS
        Intent nextClick = new Intent(this, NextListenerReceiver.class);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextClick, PendingIntent.FLAG_IMMUTABLE);

        expandedViews.setOnClickPendingIntent(R.id.rightArrow,nextPendingIntent);

        Intent prevClick = new Intent(this, PreviousListenerReceiver.class);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevClick, PendingIntent.FLAG_IMMUTABLE);

        expandedViews.setOnClickPendingIntent(R.id.leftArrow,prevPendingIntent);

        notification =  new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Todo List")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setCustomContentView(collapsedViews)
                .setCustomBigContentView(expandedViews)
                .build();
        startForeground(1,notification);

        setupArrayList();
//        setupViewFlipper();


        return START_REDELIVER_INTENT;
    }

    private void setupArrayList(){

        Cursor c = dbHelper.getData();
        if (c.moveToFirst()) {
            do {
                TodoModelClass todoModelClass = new TodoModelClass();
                todoModelClass.setText(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT)));
                todoModelClass.setDate(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_DATE)));
                todoArrayList.add(todoModelClass);

            } while (c.moveToNext());
        }
        Toast.makeText(this, ""+ todoArrayList.size(), Toast.LENGTH_SHORT).show();
    }

    public void nextClickListener(){
//        for (int i = 0;i <=todoArrayList.size() -1; i++){
////            TextView textView = new TextView(this);
////            textView.setText(todoArrayList.get(i).getText());
////            textView.setGravity(Gravity.CENTER);
////            viewFlipper.addView(textView);
////            viewFlipper.setAutoStart(true);
//        }
        if (todoArrayList.size() > 0 && i  != todoArrayList.size()-1){
            i =  i+ 1;
        } else {
            i = 0;
        }
        expandedViews.setTextViewText(R.id.notification_expanded_title,todoArrayList.get(i).getText());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }
}
