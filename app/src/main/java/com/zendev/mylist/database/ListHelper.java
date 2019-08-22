package com.zendev.mylist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zendev.mylist.model.List;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.zendev.mylist.database.DatabaseContract.ListColumns.DATE;
import static com.zendev.mylist.database.DatabaseContract.ListColumns.DESCRIPTION;
import static com.zendev.mylist.database.DatabaseContract.ListColumns.TITLE;
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

    public ArrayList<List> getAllList() {
        ArrayList<List> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        List list;
        if (cursor.getCount() > 0) {
            do {
                list = new List();
                list.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                list.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                list.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                list.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(list);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertLit(List list) {
        ContentValues args = new ContentValues();
        args.put(TITLE, list.getTitle());
        args.put(DESCRIPTION, list.getDescription());
        args.put(DATE, list.getDate());
        return database.insert(DATABASE_TABLE, null, args);
    }
}
