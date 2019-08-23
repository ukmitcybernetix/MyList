package com.zendev.mylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zendev.mylist.adapter.ListAdapter;
import com.zendev.mylist.database.ListHelper;
import com.zendev.mylist.model.List;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.zendev.mylist.ListAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadListCallback {

    private RecyclerView rvList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private ListAdapter adapter;
    private ListHelper listHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.title);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setHasFixedSize(true);

        listHelper = ListHelper.getInstance(getApplicationContext());
        listHelper.open();

        progressBar = findViewById(R.id.progressbar);
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        adapter = new ListAdapter(this);
        rvList.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadListAsync(listHelper, this).execute();
        } else {
            ArrayList<List> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setList(list);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getList());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            Intent intent = new Intent(MainActivity.this, ListAddUpdateActivity.class);
            startActivityForResult(intent, ListAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<List> arrayList) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setList(arrayList);
    }

    private static class LoadListAsync extends AsyncTask<Void, Void, ArrayList<List>> {
        private final WeakReference<ListHelper> weakListHelper;
        private final WeakReference<LoadListCallback> weakCallback;

        private LoadListAsync(ListHelper listHelper, LoadListCallback callback) {
            weakListHelper = new WeakReference<>(listHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<List> doInBackground(Void... voids) {

            return weakListHelper.get().getAllList();
        }

        @Override
        protected void onPostExecute(ArrayList<List> lists) {
            super.onPostExecute(lists);

            weakCallback.get().postExecute(lists);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == ListAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == ListAddUpdateActivity.RESULT_ADD) {
                    List list = data.getParcelableExtra(ListAddUpdateActivity.EXTRA_LIST);
                    adapter.addList(list);
                    rvList.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage("One Item Successfully Added");
                }
            } else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == ListAddUpdateActivity.RESULT_UPDATE) {
                    List list = data.getParcelableExtra(ListAddUpdateActivity.EXTRA_LIST);
                    int position = data.getIntExtra(ListAddUpdateActivity.EXTRA_POSITION, 0);
                    adapter.updateList(position, list);
                    rvList.smoothScrollToPosition(position);
                    showSnackbarMessage("One Item Successfully Changed");
                } else if (resultCode == ListAddUpdateActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(ListAddUpdateActivity.EXTRA_POSITION, 0);
                    adapter.removeList(position);
                    showSnackbarMessage("One Item Successfully Deleted");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listHelper.close();
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvList, message, Snackbar.LENGTH_SHORT).show();
    }
}
