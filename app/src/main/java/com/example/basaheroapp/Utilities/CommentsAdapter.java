package com.example.basaheroapp.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.basaheroapp.R;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private Context context;
    private ArrayList<String> listname;
    private ArrayList<String> listcontent;
    private ArrayList<String> listcreated;


    public CommentsAdapter(Context context, ArrayList<String> listname, ArrayList<String> listcontent, ArrayList<String> listcreated) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listname = listname;
        this.listcontent = listcontent;
        this.listcreated = listcreated;
    }

    @Override
    public int getCount() {
        return listname.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.comment_list, null);
        TextView name = view.findViewById(R.id.comment_name);
        TextView content = view.findViewById(R.id.comment_content);
        TextView date = view.findViewById(R.id.comment_date);

        name.setText(listname.get(i));
        content.setText(listcontent.get(i));
        date.setText(listcreated.get(i));

        return view;
    }
}
