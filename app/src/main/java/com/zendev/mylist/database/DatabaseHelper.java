package com.zendev.mylist.database;

public class DatabaseHelper {

    public static String DATABASE_NAME = "dbmylist";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_LIST = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_LIST,
            DatabaseContract.ListColumns._ID,
            DatabaseContract.ListColumns.TITLE,
            DatabaseContract.ListColumns.DESCRIPTION,
            DatabaseContract.ListColumns.DATE
    );
}
