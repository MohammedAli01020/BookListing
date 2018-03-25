package com.example.mohamed.booklisting.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String mImage;
    private String mTitle;
    private String mAuthor;
    private String mPreviewLink;

    /**
     * @param mImage       is the thumbnail image of the book.
     * @param mTitle       is the title of the book.
     * @param mAuthor      is the name of the publisher.
     * @param mPreviewLink is the link of the book.
     */
    Book(String mImage, String mTitle, String mAuthor, String mPreviewLink) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mPreviewLink = mPreviewLink;
    }

    private Book(Parcel in) {
        mImage = in.readString();
        mTitle = in.readString();
        mAuthor = in.readString();
        mPreviewLink = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    String getmImage() {
        return mImage;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmAuthor() {
        return mAuthor;
    }

    public String getmPreviewLink() {
        return mPreviewLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImage);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mPreviewLink);
    }
}
