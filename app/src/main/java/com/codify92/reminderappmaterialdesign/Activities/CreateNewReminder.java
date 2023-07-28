package com.codify92.reminderappmaterialdesign.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteConstants;
import com.codify92.reminderappmaterialdesign.SQLiteDatabse.SQLiteDatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateNewReminder extends AppCompatActivity {

    EditText reminderTitle;
    EditText reminderSubtext;

    ImageView mFabDone;

    ArrayList<TodoModelClass> todoArrayList;

    public static SQLiteDatabaseHelper dbHelper;
    public static SQLiteDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_new_reminder);

        setAnimationForTransition();
        initialize();
        saveReminder();
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

                //TODO:Add To Array List
                TodoModelClass modelClass = new TodoModelClass();
                modelClass.setText(EnteredText);
                modelClass.setSubtext(Subtext);
                modelClass.setDate(Date);
                modelClass.setCompleted(false);
                modelClass.setPriority(1);
                todoArrayList.add(modelClass);

                //TODO: Save To Database
                ContentValues cv = new ContentValues();
                cv.put(SQLiteConstants.TodoEntry.COLUMN_TITLE_TEXT, EnteredText);
                cv.put(SQLiteConstants.TodoEntry.COLUMN_SUBTEXT, Subtext);
                cv.put(SQLiteConstants.TodoEntry.COLUMN_DATE, String.valueOf(Date));
                mDatabase.insert(SQLiteConstants.TodoEntry.TABLE_NAME, null, cv);

                finish();
            }
        });


    }

}