package com.codify92.reminderappmaterialdesign.Activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.codify92.reminderappmaterialdesign.Adapter.TodoAdapter;
import com.codify92.reminderappmaterialdesign.Others.BottomSheetDialog;
import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TodoModelClass> todoArrayList;
    TodoAdapter mAdapter;
    //TODO:DATABASE
    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;

    public static boolean cameFromSecondScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        setAnimationForTransition();

        todoArrayList = new ArrayList<>();
        setupDatabase();
        getDataFromDatabaseAndAddToArrayList();
        mAdapter = new TodoAdapter(this, todoArrayList);

        mRecyclerView = findViewById(R.id.todayRecyclerView);
        layoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        addTaskClickListener();
    }

    private void setAnimationForTransition(){

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.today),true);
        fade.excludeTarget(decor.findViewById(R.id.menuRight),true);
        fade.excludeTarget(decor.findViewById(R.id.backmain),true);
        fade.excludeTarget(decor.findViewById(R.id.bottomCard),true);


        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

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
                todoModelClass.setSubtext(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_SUBTEXT)));

                todoModelClass.setDate(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_DATE)));
                todoModelClass.setChosenColor(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(SQLiteConstants.TodoEntry.COLUMN_BACKGROUND_COLOR))));
                todoArrayList.add(todoModelClass);
            } while (c.moveToNext());
        }
    }

    private void addTaskClickListener() {
        ImageView fabAddReminder = findViewById(R.id.fabAddTask);
        TextView textView = findViewById(R.id.today);
        ImageView menuRight = findViewById(R.id.menuRight);
        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,CreateNewReminder.class);
                Pair [] pair = new Pair[3];
                pair[0] = new Pair<View,String> (fabAddReminder,ViewCompat.getTransitionName(fabAddReminder));
                pair[1] = new Pair<View,String> (textView,ViewCompat.getTransitionName(textView));
                pair[2] = new Pair<View,String> (menuRight,ViewCompat.getTransitionName(menuRight));


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,pair);
                startActivity(intent,options.toBundle());

            }
        });
    }

//    @Override
//    public void onSaveClicked(String EnteredText, CharSequence Date) {
//
//        //TODO:Add To Array List
//        TodoModelClass modelClass = new TodoModelClass();
//        modelClass.setText(EnteredText);
//        modelClass.setDate(Date);
//        modelClass.setCompleted(false);
//        modelClass.setPriority(1);
//        todoArrayList.add(modelClass);
//
//        //TODO: Save To Database
//        ContentValues cv = new ContentValues();
//        cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
//        cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, String.valueOf(Date));
//        mDatabase.insert(SQLiteConstants.TodoEntry.TABLE_NAME, null, cv);
//
//        //TODO:Notify Adapter
//        mAdapter.notifyDataSetChanged();
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameFromSecondScreen){
            recreate();
            cameFromSecondScreen = false;
        }
    }
}