package com.codify92.reminderappmaterialdesign.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateNewReminder extends AppCompatActivity implements View.OnClickListener {

    EditText reminderTitle;
    EditText reminderSubtext;

    ImageView mFabDone;
    ImageView mBackButton;

    ImageView datePicker;
    CardView mBackgroundGreen;
    CardView mBackgroundLightPurple;
    CardView mBackgroundPink;
    CardView mBackgroundOrange;
    CardView mBackgroundBlue;
    CardView mBackgroundDarkPurple;


    CardView mBottomCard;
    ArrayList<TodoModelClass> todoArrayList;

    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;

    int chosenBackgroundColor = 1;

    String chosenDateForReminder = "";
    String chosenTimeForReminder = "";

    String finalDateAndTime;

    boolean isUpdating = false;
    int position;

    String titleToUpdate;
    String subtextToUpdate;

    InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_new_reminder);

        setAnimationForTransition();
        initialize();
        getIntentData();
        saveReminder();
        otherFunctions();
        loadInterstitialAd();
    }

    private void setAnimationForTransition() {

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.bottomCard), true);
        fade.excludeTarget(decor.findViewById(R.id.backBtn), true);
        fade.excludeTarget(decor.findViewById(R.id.pinBbtn), true);
        fade.excludeTarget(decor.findViewById(R.id.backsecond), true);


        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

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
                interstitialAdCallbacks();

            }
        });

    }

    private void interstitialAdCallbacks(){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                finish();
                MainActivity.cameFromSecondScreen = true;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                finish();
                MainActivity.cameFromSecondScreen = true;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                finish();
                MainActivity.cameFromSecondScreen = true;
            }
        });
    }

    private void initialize() {


        reminderTitle = findViewById(R.id.reminderTitle);
        reminderSubtext = findViewById(R.id.reminderDetails);
        mFabDone = findViewById(R.id.fabDone);
        mBackButton = findViewById(R.id.backBtn);
        mBottomCard = findViewById(R.id.bottomCard);
        datePicker = findViewById(R.id.dateBtn);

        //Bottom Color Selector For Reminder ID's
        mBackgroundBlue = findViewById(R.id.cardBlue);
        mBackgroundGreen = findViewById(R.id.cardGreen);
        mBackgroundPink = findViewById(R.id.cardPink);
        mBackgroundOrange = findViewById(R.id.cardOrange);
        mBackgroundDarkPurple = findViewById(R.id.cardDarkPurple);
        mBackgroundLightPurple = findViewById(R.id.cardLightPurple);
        //Adding Click Listener To These Buttons
        mBackgroundDarkPurple.setOnClickListener(this);
        mBackgroundOrange.setOnClickListener(this);
        mBackgroundBlue.setOnClickListener(this);
        mBackgroundLightPurple.setOnClickListener(this);
        mBackgroundGreen.setOnClickListener(this);
        mBackgroundPink.setOnClickListener(this);


        todoArrayList = new ArrayList<>();

        //Database Setup
        dbHelper = new SQLiteDatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {

        } else {

            try {
                titleToUpdate = getIntent().getStringExtra("title");
                subtextToUpdate = getIntent().getStringExtra("subtext");
                isUpdating = getIntent().getBooleanExtra("isupdate", false);
                position = getIntent().getIntExtra("position",0);
                finalDateAndTime = getIntent().getStringExtra("date");
                if (!titleToUpdate.equals("")) {
                    reminderTitle.setText(titleToUpdate);
                }
                if (!subtextToUpdate.equals("")) {
                    reminderSubtext.setText(subtextToUpdate);
                }
            } catch (Exception e) {

            }
        }
    }

    private void saveReminder() {
        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUpdating) {
                    String EnteredText = reminderTitle.getText().toString();
                    String Subtext = reminderSubtext.getText().toString();
                    finalDateAndTime = chosenDateForReminder + chosenTimeForReminder;

                    if (!EnteredText.equals("") || !Subtext.equals("")) {
                        //TODO:Add To Array List
                        TodoModelClass modelClass = new TodoModelClass();
                        modelClass.setText(EnteredText);
                        modelClass.setSubtext(Subtext);
                        modelClass.setChosenColor(chosenBackgroundColor);
                        modelClass.setDate(finalDateAndTime);
                        todoArrayList.add(modelClass);

                        //TODO: Save To Database
                        ContentValues cv = new ContentValues();
                        cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
                        cv.put(SQLiteConstants.TodoEntry.COLUMN_SUBTEXT, Subtext);
                        cv.put(String.valueOf(SQLiteConstants.TodoEntry.COLUMN_BACKGROUND_COLOR), chosenBackgroundColor);
                        cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, String.valueOf(finalDateAndTime));
                        mDatabase.insert(SQLiteConstants.TodoEntry.TABLE_NAME, null, cv);

                        if (mInterstitialAd!= null){
                            mInterstitialAd.show(CreateNewReminder.this);
                        } else {
                            finish();
                            MainActivity.cameFromSecondScreen = true;
                        }

                    } else
                        Toast.makeText(CreateNewReminder.this, "Please Enter Some Text!", Toast.LENGTH_SHORT).show();

                } else {
                    updateReminder();
                }
            }
        });

    }

    private void updateReminder(){
        String EnteredText = reminderTitle.getText().toString();
        String Subtext = reminderSubtext.getText().toString();
        finalDateAndTime = chosenDateForReminder + chosenTimeForReminder;

        if (!EnteredText.equals("") || !Subtext.equals("")) {
            //TODO: Update In Database
            ContentValues cv = new ContentValues();
            cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
            cv.put(SQLiteConstants.TodoEntry.COLUMN_SUBTEXT, Subtext);
            cv.put(String.valueOf(SQLiteConstants.TodoEntry.COLUMN_BACKGROUND_COLOR), chosenBackgroundColor);
            cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, finalDateAndTime);

            if (!titleToUpdate.equals("")){
                mDatabase.update(SQLiteConstants.TodoEntry.TABLE_NAME,cv,"text = ?", new String[]{titleToUpdate});
            } else {
                mDatabase.update(SQLiteConstants.TodoEntry.TABLE_NAME,cv,"subtext = ?", new String[]{subtextToUpdate});
            }

            finish();
            MainActivity.cameFromSecondScreen = true;
        } else
            Toast.makeText(CreateNewReminder.this, "Please Enter Some Text!", Toast.LENGTH_SHORT).show();

    }

    private void otherFunctions() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateDialog();
            }
        });
    }

    private void getDateDialog() {
        //Getting Current Date To Set On Date Picker
        Calendar calendar = Calendar.getInstance();

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //Creating Date Picker Dialog
        DatePickerDialog dialog = new DatePickerDialog(CreateNewReminder.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Formatting The Date And Adding To Date Variable
                if (year == mYear && month == mMonth && day == mDay) {
                    chosenDateForReminder = "Today";
                } else if (year == mYear && month == mMonth && day == mDay + 1) {
                    chosenDateForReminder = "Tomorrow";
                } else {
                    String[] nameOfMonth = {"Jan", "Feb", "March", "April",
                            "May", "June", "july", "Aug",
                            "Sep", "Oct", "Nov", "Dec"};

                    chosenDateForReminder = day + " " + nameOfMonth[month];
                }

                getTimeDialog();
            }
        }, mYear, mMonth, mDay);

        dialog.show();
    }

    private void getTimeDialog() {
        Calendar calendar = Calendar.getInstance();

        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);


        TimePickerDialog dialog = new TimePickerDialog(CreateNewReminder.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (minute == 0) {
                    chosenTimeForReminder = ", " + hour + ":" + "00";
                } else if (minute < 10){
                    chosenTimeForReminder = ", " + hour + ":" + "0" + minute;
                } else {
                    chosenTimeForReminder = ", " + hour + ":" + minute;
                }
            }
        }, mHour, mMinute, false);
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardBlue) {
            showCheckForBottomCards(1);
            chosenBackgroundColor = 1;
        } else if (v.getId() == R.id.cardGreen) {
            showCheckForBottomCards(2);
            chosenBackgroundColor = 2;
        } else if (v.getId() == R.id.cardPink) {
            showCheckForBottomCards(3);
            chosenBackgroundColor = 3;
        } else if (v.getId() == R.id.cardOrange) {
            showCheckForBottomCards(4);
            chosenBackgroundColor = 4;
        } else if (v.getId() == R.id.cardLightPurple) {
            showCheckForBottomCards(5);
            chosenBackgroundColor = 5;
        } else if (v.getId() == R.id.cardDarkPurple) {
            showCheckForBottomCards(6);
            chosenBackgroundColor = 6;
        }
    }

    private void showCheckForBottomCards(int number) {
        ImageView checkBlue = findViewById(R.id.checkBlue);
        ImageView checkGreen = findViewById(R.id.checkGreen);
        ImageView checkPink = findViewById(R.id.checkPink);
        ImageView checkOrange = findViewById(R.id.checkOrange);
        ImageView checkLightPurple = findViewById(R.id.checkLightPurple);
        ImageView checkDarkPurple = findViewById(R.id.checkDarkPurple);

        checkBlue.setVisibility(View.GONE);
        checkGreen.setVisibility(View.GONE);
        checkPink.setVisibility(View.GONE);
        checkOrange.setVisibility(View.GONE);
        checkLightPurple.setVisibility(View.GONE);
        checkDarkPurple.setVisibility(View.GONE);

        if (number == 1) {
            checkBlue.setVisibility(View.VISIBLE);
        } else if (number == 2) {
            checkGreen.setVisibility(View.VISIBLE);
        } else if (number == 3) {
            checkPink.setVisibility(View.VISIBLE);
        } else if (number == 4) {
            checkOrange.setVisibility(View.VISIBLE);
        } else if (number == 5) {
            checkLightPurple.setVisibility(View.VISIBLE);
        } else if (number == 6) {
            checkDarkPurple.setVisibility(View.VISIBLE);
        }
    }
}