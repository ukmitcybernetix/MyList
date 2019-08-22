package com.zendev.mylist.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_LIST = "list";

    static final class ListColumns implements BaseColumns {

        static String TITLE = "title";
        static String DESCRIPTION = "description";
        static String DATE = "date";

    }
}
