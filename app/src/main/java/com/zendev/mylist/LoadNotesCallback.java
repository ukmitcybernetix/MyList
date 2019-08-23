package com.zendev.mylist;

import com.zendev.mylist.model.List;

import java.util.ArrayList;

public interface LoadNotesCallback {
    void preExecute();

    void postExecute(ArrayList<List> lists);
}
