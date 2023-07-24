package com.codify92.reminderappmaterialdesign.SQLiteDatabse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TodoList.db";
    public static final int DATABASE_VERSION = 2;

    public SQLiteDatabaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TODO_LIST_TABLE = "CREATE TABLE " +
                SQLiteConstants.TodoEntry.TABLE_NAME + " (" +
                SQLiteConstants.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT + " TEXT NOT NULL, " +
                SQLiteConstants.TodoEntry.COLUMN_SUBTEXT + " TEXT NOT NULL, " +
                SQLiteConstants.TodoEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                SQLiteConstants.TodoEntry.COLUMN_DATE + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_TODO_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQLiteConstants.TodoEntry.TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData (){
        SQLiteDatabase db = this.getWritableDatabase();
        String rawQuery = "SELECT * FROM " + SQLiteConstants.TodoEntry.TABLE_NAME;
        Cursor data = db.rawQuery(rawQuery,null);
        return data;
    }

    public void removeFromDatabase(String title){

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = String.format("DELETE FROM todoList WHERE text='%s';",title  );
        db.execSQL(selectQuery);
    }

    public String getName (String id){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = String.format("SELECT  * FROM studentInfo WHERE studentid='%s';",id);
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                int nameFieldColumnIndex = cursor.getColumnIndex(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT);
                String name = cursor.getString(nameFieldColumnIndex);
                return name;
            }
        }
        return "Empty";
    }
}
