package com.example.mohamed.booklisting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.booklisting.data.Book;
import com.example.mohamed.booklisting.data.BooksAdapter;
import com.example.mohamed.booklisting.data.QueryUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String URL_ADDRESS = "https://www.googleapis.com/books/v1/volumes?q=";
    private ListView mBookListView;
    private EditText searchBox;
    private TextView mEmptyStateTextView;
    private ArrayList<Book> mBooks;

    private static final String LIST_BOOKS = "listBooks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mBooks = savedInstanceState.getParcelableArrayList(LIST_BOOKS);
        }

        mBookListView = findViewById(R.id.list_books);
        searchBox = findViewById(R.id.search_box);
        ImageButton searchButton = findViewById(R.id.search);

        mEmptyStateTextView = findViewById(R.id.empty_view);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchBox.getText().toString();
                if (TextUtils.isEmpty(search)) {
                    return;
                }
                if (isOnline(getApplicationContext())) {
                    URL_ADDRESS = "https://www.googleapis.com/books/v1/volumes?q=" + search;
                    BookTask task = new BookTask();
                    task.execute(URL_ADDRESS);
                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });


        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = (Book) adapterView.getItemAtPosition(i);
                Intent bookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getmPreviewLink()));

                if (bookIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(bookIntent);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LIST_BOOKS, mBooks);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BooksAdapter adapter = new BooksAdapter(this, mBooks);
        mBookListView.setAdapter(adapter);
    }

    private boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void updateUi(ArrayList<Book> books) {
        mBooks = books;
        BooksAdapter mAdapter = new BooksAdapter(this, books);

        if (books.size() == 0) {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_books);
            mBookListView.setAdapter(mAdapter);
        } else {
            mEmptyStateTextView.setVisibility(View.INVISIBLE);
            mBookListView.setAdapter(mAdapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BookTask extends AsyncTask<String, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            return QueryUtils.fetchBookData(urls[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books == null) {
                return;
            }
            updateUi(books);
        }
    }
}
