package com.codify92.reminderappmaterialdesign.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateNewReminder extends AppCompatActivity implements View.OnClickListener {

    EditText reminderTitle;
    EditText reminderSubtext;

    ImageView mFabDone;
    ImageView mBackButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_new_reminder);

        setAnimationForTransition();
        initialize();
        saveReminder();
        otherFunctions();
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

    private void initialize() {


        reminderTitle = findViewById(R.id.reminderTitle);
        reminderSubtext = findViewById(R.id.reminderDetails);
        mFabDone = findViewById(R.id.fabDone);
        mBackButton = findViewById(R.id.backBtn);
        mBottomCard = findViewById(R.id.bottomCard);

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

    private void saveReminder() {
        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EnteredText = reminderTitle.getText().toString();
                String Subtext = reminderSubtext.getText().toString();
                String Date = "July 4,2023";

                if (!EnteredText.equals("") || !Subtext.equals("")){
                    //TODO:Add To Array List
                    TodoModelClass modelClass = new TodoModelClass();
                    modelClass.setText(EnteredText);
                    modelClass.setSubtext(Subtext);
                    modelClass.setChosenColor(chosenBackgroundColor);
                    modelClass.setDate(Date);
                    todoArrayList.add(modelClass);

                    //TODO: Save To Database
                    ContentValues cv = new ContentValues();
                    cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
                    cv.put(SQLiteConstants.TodoEntry.COLUMN_SUBTEXT, Subtext);
                    cv.put(String.valueOf(SQLiteConstants.TodoEntry.COLUMN_BACKGROUND_COLOR),chosenBackgroundColor);
                    cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, String.valueOf(Date));
                    mDatabase.insert(SQLiteConstants.TodoEntry.TABLE_NAME, null, cv);

                    finish();
                    MainActivity.cameFromSecondScreen = true;
                } else Toast.makeText(CreateNewReminder.this, "Please Enter Some Text!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void otherFunctions(){
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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