package com.example.mohamed.booklisting.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.booklisting.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BooksAdapter extends ArrayAdapter<Book> {
    public BooksAdapter(@NonNull Context context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book currentBook = getItem(position);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder.image = convertView.findViewById(R.id.image);
            holder.title = convertView.findViewById(R.id.title);
            holder.author = convertView.findViewById(R.id.author);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(currentBook.getmImage()).into(holder.image);
        holder.title.setText(currentBook.getmTitle());
        holder.author.setText(currentBook.getmAuthor());

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView author;
    }
}
