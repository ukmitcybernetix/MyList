package com.zendev.mylist.database;

import android.provider.BaseColumns;

class DatabaseContract {

    static final String TABLE_LIST = "list";

    static final class ListColumns implements BaseColumns {

        static final String TITLE = "title";
        static final String DESCRIPTION = "description";
        static final String DATE = "date";

    }
}
