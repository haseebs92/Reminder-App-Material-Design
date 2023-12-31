package com.codify92.reminderappmaterialdesign.SQLiteDatabse;

import android.provider.BaseColumns;

public class SQLiteConstants {

    private SQLiteConstants() { }

    public static final class TodoEntry implements BaseColumns {

        public static final String TABLE_NAME  = "reminderList";
        public static final String COLUMN_TITLE_TEXT  = "text";

        public static final String COLUMN_SUBTEXT  = "subtext";
        public static final String COLUMN_BACKGROUND_COLOR  = "color";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
