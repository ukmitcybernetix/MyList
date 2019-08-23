package com.zendev.mylist.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zendev.mylist.CustomOnItemClickListener;
import com.zendev.mylist.ListAddUpdateActivity;
import com.zendev.mylist.R;
import com.zendev.mylist.model.List;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private ArrayList<List> arrayList = new ArrayList<>();
    private Activity activity;

    public ArrayList<List> getList() {
        return arrayList;
    }

    public ListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setList(ArrayList<List> arrayList) {
        if (arrayList.size() > 0) {
            this.arrayList.clear();
        }
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addList(List list) {
        this.arrayList.add(list);
        notifyItemInserted(arrayList.size() - 1);
    }

    public void updateList(int position, List list) {
        this.arrayList.set(position, list);
        notifyItemChanged(position, list);
    }

    public void removeList(int position) {
        this.arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.tvTitle.setText(arrayList.get(position).getTitle());
        holder.tvDate.setText(arrayList.get(position).getDate());
        holder.tvDescription.setText(arrayList.get(position).getDescription());

        holder.cvList.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, ListAddUpdateActivity.class);
                intent.putExtra(ListAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(ListAddUpdateActivity.EXTRA_LIST, arrayList.get(position));
                activity.startActivityForResult(intent, ListAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle, tvDescription, tvDate;
        final CardView cvList;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvList = itemView.findViewById(R.id.cv_item_list);
        }
    }
}
