package com.codify92.reminderappmaterialdesign.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codify92.reminderappmaterialdesign.ForgroundService.TodoListService;


public class NextListenerReceiver extends BroadcastReceiver {
    public static int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        TodoListService todoListService = new TodoListService();
        todoListService.nextClickListener();

        NotificationManager service = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        service.notify(1, TodoListService.notification);

    }
}
