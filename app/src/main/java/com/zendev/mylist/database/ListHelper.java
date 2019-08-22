package com.zendev.mylist.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.zendev.mylist.database.DatabaseContract.TABLE_LIST;

public class ListHelper {

    private static final String DATABASE_TABLE = TABLE_LIST;
    private static DatabaseHelper databaseHelper;
    private static ListHelper INSTANCE;

    private static SQLiteDatabase database;

    public ListHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static ListHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ListHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getReadableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }
}
