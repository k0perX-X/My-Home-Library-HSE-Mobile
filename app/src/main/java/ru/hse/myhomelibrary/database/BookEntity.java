package ru.hse.myhomelibrary.database;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "book",
        indices = {
                @Index(value = {"name"}, unique = true),
                @Index(value = {"author"}, unique = true)
        }
)
public class BookEntity {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name = "";

    @ColumnInfo(name = "author")
    @NonNull
    public String author = "";

    @ColumnInfo(name = "year_of_publishing")
    public int yearPublishing;

    @ColumnInfo(name = "place_of_publishing")
    public int placePublishing;

    @ColumnInfo(name = "path_to_picture")
    public Uri imageUri;

    @ColumnInfo(name = "review")
    public String review;

    @ColumnInfo(name = "favorite")
    @NonNull
    public Integer favorite = 0;
}
