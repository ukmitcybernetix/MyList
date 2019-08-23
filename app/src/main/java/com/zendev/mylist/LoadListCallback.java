package com.zendev.mylist;

import com.zendev.mylist.model.List;

import java.util.ArrayList;

interface LoadListCallback {
    void preExecute();

    void postExecute(ArrayList<List> lists);
}
