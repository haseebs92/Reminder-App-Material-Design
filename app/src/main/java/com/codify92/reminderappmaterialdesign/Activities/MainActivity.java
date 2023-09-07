package com.codify92.reminderappmaterialdesign.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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

    ImageView mMenu;

    TextView mTitle;

    ConstraintLayout mNoReminderLayout;

    InterstitialAd mInterstitialAd;

    public static boolean adShown = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        setAnimationForTransition();

        mMenu = findViewById(R.id.menuRight);
        mTitle = findViewById(R.id.titleText1);
        mNoReminderLayout = findViewById(R.id.noReminderLayout);

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
        initializeAds();
    }

    private void reminderClickListener() {
        mAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

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
            }
        });

        mAdapter.setOnItemLongClickListener(new TodoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Reminder")
                        .setMessage("Are you sure you want to delete this reminder?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                String titleToDelete = todoArrayList.get(position).getText();
                                if (!titleToDelete.equals("")) {
                                    mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
                                            "text = ?", new String[]{titleToDelete});
                                } else {
                                    titleToDelete = todoArrayList.get(position).getSubtext();
                                    mDatabase.delete(SQLiteConstants.TodoEntry.TABLE_NAME,
                                            "subtext = ?", new String[]{titleToDelete});
                                }
                                todoArrayList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_delete)
                        .show();

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
                mNoReminderLayout.setVisibility(View.GONE);
            } while (c.moveToNext());
        } else {
            mNoReminderLayout.setVisibility(View.VISIBLE);
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
                Pair[] pair = new Pair[2];
                pair[0] = new Pair<View, String>(fabAddReminder, ViewCompat.getTransitionName(fabAddReminder));
                pair[1] = new Pair<View, String>(textView, ViewCompat.getTransitionName(textView));
//                pair[2] = new Pair<View, String>(menuRight, ViewCompat.getTransitionName(menuRight));


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
                startActivity(intent, options.toBundle());

            }
        });
    }

    private void initializeAds(){
        MobileAds.initialize(this, initializationStatus -> {
            // Ads have been initialized
            showBannerAd();
            if (!adShown){
                loadInterstitialAd();
                adShown = true;
            }

        });


    }

    private void showBannerAd(){
        AdView adView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.load(this,getResources().getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MainActivity.this);

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