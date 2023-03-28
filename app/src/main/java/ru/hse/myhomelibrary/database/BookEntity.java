package ru.hse.myhomelibrary.database;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "book",
        indices = {
                @Index(value = {"name"}),
                @Index(value = {"author"})
        }
)
public class BookEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name = "";

    @ColumnInfo(name = "author")
    @NonNull
    public String author = "";

    @ColumnInfo(name = "year_of_publishing")
    public Integer yearPublishing;

    @ColumnInfo(name = "place_of_publishing")
    public String placePublishing;

    @ColumnInfo(name = "path_to_picture")
    public Uri imageUri;

    @ColumnInfo(name = "review")
    public String review;

    @ColumnInfo(name = "favorite")
    @NonNull
    public Integer favorite = 0;

    @ColumnInfo(name= "date_of_create")
    @NonNull
    public Date createDate = new Date();

    @ColumnInfo(name= "date_of_edit")
    @NonNull
    public Date editDate = new Date();
}
