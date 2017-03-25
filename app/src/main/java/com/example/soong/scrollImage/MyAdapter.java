package com.example.soong.scrollimage;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Soong on 2017/3/25.
 */

public class MyAdapter extends ArrayAdapter<String> {


    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView ==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_style,null);
        }else{
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.list_text);
        textView.setText(getItem(position));
        return view;
    }
}
