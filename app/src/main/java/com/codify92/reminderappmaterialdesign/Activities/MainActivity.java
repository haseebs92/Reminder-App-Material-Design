package com.codify92.reminderappmaterialdesign.Activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;


import com.codify92.reminderappmaterialdesign.Adapter.TodoAdapter;
import com.codify92.reminderappmaterialdesign.Others.BottomSheetDialog;
import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TodoModelClass> todoArrayList;
    TodoAdapter mAdapter;
    //TODO:DATABASE
    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoArrayList = new ArrayList<>();
        setupDatabase();
        getDataFromDatabaseAndAddToArrayList();
        mAdapter = new TodoAdapter(this, todoArrayList);

        mRecyclerView = findViewById(R.id.todayRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        addTaskClickListener();
    }

    private void setupDatabase() {
        dbHelper = new SQLiteDatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
    }

    private void getDataFromDatabaseAndAddToArrayList() {
        Cursor c = dbHelper.getData();
        if (c.moveToFirst()) {
            do {
                TodoModelClass todoModelClass = new TodoModelClass();
                todoModelClass.setText(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT)));
                todoModelClass.setDate(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_DATE)));
                todoArrayList.add(todoModelClass);
            } while (c.moveToNext());
        }
    }

    private void addTaskClickListener() {
        FloatingActionButton button = findViewById(R.id.fabAddTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "First");
            }
        });
    }

    @Override
    public void onSaveClicked(String EnteredText, CharSequence Date) {

        //TODO:Add To Array List
        TodoModelClass modelClass = new TodoModelClass();
        modelClass.setText(EnteredText);
        modelClass.setDate(Date);
        modelClass.setCompleted(false);
        modelClass.setPriority(1);
        todoArrayList.add(modelClass);

        //TODO: Save To Database
        ContentValues cv = new ContentValues();
        cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
        cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, String.valueOf(Date));
        mDatabase.insert(SQLiteConstants.TodoEntry.TABLE_NAME, null, cv);

        //TODO:Notify Adapter
        mAdapter.notifyDataSetChanged();


    }


}