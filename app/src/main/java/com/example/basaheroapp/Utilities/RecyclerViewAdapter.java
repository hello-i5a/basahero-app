package com.example.basaheroapp.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.basaheroapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AppViewHolder>{

    private ArrayList<Item> list = new ArrayList<Item>();

    public RecyclerViewAdapter(ArrayList<Item> list) {
        this.list = list;
    }

    public static class AppViewHolder extends ViewHolder {
        View view;
        public AppViewHolder (@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        View view = holder.view;

//        ImageView img = view.findViewById(R.id.bookimg);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView date = view.findViewById(R.id.date);
        TextView desc = view.findViewById(R.id.description);
        TextView id = view.findViewById(R.id.id);

//        Glide.with(context)
//                .load("https://zibrpedawwcozzwcepfg.supabase.co/storage/v1/object/public/book_img/" + list.get(position).getImg())
//                .placeholder(R.color.borderColor)
//                .into(img);
        title.setText(list.get(position).getTitle());
        author.setText(list.get(position).getAuthor());
        date.setText(list.get(position).getDate());
        desc.setText(list.get(position).getDescription());
        id.setText(String.valueOf(list.get(position).getId()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
