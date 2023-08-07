package com.codify92.reminderappmaterialdesign.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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


import com.codify92.reminderappmaterialdesign.Adapter.TodoAdapter;
import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TodoModelClass> todoArrayList;
    TodoAdapter mAdapter;
    //TODO:DATABASE
    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;

    public static boolean cameFromSecondScreen = false;

    private boolean isSelectingItems = false;

    ImageView mClose;
    ImageView mMenu;

    TextView mTitle;

    List<Integer> arrayOfSelectedItems;

    FloatingActionButton mDeleteFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        setAnimationForTransition();

        mClose = findViewById(R.id.close);
        mMenu = findViewById(R.id.menuRight);
        mTitle = findViewById(R.id.titleText1);
        mDeleteFab = findViewById(R.id.deleteFab);

        arrayOfSelectedItems = new ArrayList<Integer>();
        todoArrayList = new ArrayList<>();
        setupDatabase();
        getDataFromDatabaseAndAddToArrayList();
        mAdapter = new TodoAdapter(this, todoArrayList);

        mRecyclerView = findViewById(R.id.todayRecyclerView);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        addTaskClickListener();
        reminderClickListener();
        onViewsClickListeners();
    }

    private void reminderClickListener() {
        mAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!isSelectingItems) {
                    String title = todoArrayList.get(position).getText();
                    String subtext = todoArrayList.get(position).getSubtext();
                    String date = String.valueOf(todoArrayList.get(position).getDate());

                    Intent intent = new Intent(MainActivity.this, CreateNewReminder.class);
                    intent.putExtra("title", title);
                    intent.putExtra("subtext", subtext);
                    intent.putExtra("isupdate", true);
                    intent.putExtra("position", position);
                    intent.putExtra("date", date);
                    startActivity(intent);
                } else {
                    setMultipleReminderSelection(position);
                }

            }
        });

        mAdapter.setOnItemLongClickListener(new TodoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {

//                String titleToDelete = todoArrayList.get(position).getText();
//                if (!titleToDelete.equals("")){
//                    mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
//                            "text = ?", new String[]{titleToDelete});
//                } else {
//                    titleToDelete = todoArrayList.get(position).getSubtext();
//                    mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
//                            "subtext = ?", new String[]{titleToDelete});
//                }
//                todoArrayList.remove(position);
//                mAdapter.notifyItemRemoved(position);

                isSelectingItems = true;
                TodoAdapter.isSelecting = true;
                mClose.setVisibility(View.VISIBLE);
                mDeleteFab.setVisibility(View.VISIBLE);
                mMenu.setVisibility(View.GONE);
                mTitle.setText("Selecting");
                todoArrayList.get(position).setSelected(true);
                arrayOfSelectedItems.add(position);
                mAdapter.notifyDataSetChanged();

            }
        });
    }


    private void setAnimationForTransition() {

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.titleText1), true);
        fade.excludeTarget(decor.findViewById(R.id.menuRight), true);
        fade.excludeTarget(decor.findViewById(R.id.backmain), true);
        fade.excludeTarget(decor.findViewById(R.id.bottomCard), true);


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
        TextView textView = findViewById(R.id.titleText1);
        ImageView menuRight = findViewById(R.id.menuRight);
        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CreateNewReminder.class);
                Pair[] pair = new Pair[3];
                pair[0] = new Pair<View, String>(fabAddReminder, ViewCompat.getTransitionName(fabAddReminder));
                pair[1] = new Pair<View, String>(textView, ViewCompat.getTransitionName(textView));
                pair[2] = new Pair<View, String>(menuRight, ViewCompat.getTransitionName(menuRight));


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
                startActivity(intent, options.toBundle());

            }
        });
    }

    private void setMultipleReminderSelection(int position) {
        if (todoArrayList.get(position).isSelected()) {
            todoArrayList.get(position).setSelected(false);
            arrayOfSelectedItems.remove(position);
        } else {
            todoArrayList.get(position).setSelected(true);
            arrayOfSelectedItems.add(position);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void onViewsClickListeners(){
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelectingItems = false;
                TodoAdapter.isSelecting = false;
                mClose.setVisibility(View.GONE);
                mMenu.setVisibility(View.VISIBLE);
                mTitle.setText("Reminders");
                mDeleteFab.setVisibility(View.GONE);
                arrayOfSelectedItems.clear();
                for (int i = 0; i < todoArrayList.size(); i++){
                    todoArrayList.get(i).setSelected(false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mDeleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DELETE SELECTED ITEMS
                for (int i =0; i< arrayOfSelectedItems.size(); i++){
                    String titleToDelete = todoArrayList.get(arrayOfSelectedItems.get(i)).getText();
                    if (!titleToDelete.equals("")) {
                        mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
                                "text = ?", new String[]{titleToDelete});
                    } else {
                        titleToDelete = todoArrayList.get(i).getSubtext();
                        mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
                                "subtext = ?", new String[]{titleToDelete});
                    }
                    int selectedPosition = arrayOfSelectedItems.get(i);
                    todoArrayList.remove(arrayOfSelectedItems.get(i));
                    mAdapter.notifyItemRemoved(arrayOfSelectedItems.get(i));
                }
                isSelectingItems = false;
                TodoAdapter.isSelecting = false;
                mClose.setVisibility(View.GONE);
                mMenu.setVisibility(View.VISIBLE);
                mTitle.setText("Reminders");
                mDeleteFab.setVisibility(View.GONE);
                arrayOfSelectedItems.clear();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameFromSecondScreen) {
            recreate();
            cameFromSecondScreen = false;
        }
    }
}