package ru.hse.myhomelibrary.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private final DatabaseHelper db;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        db = Room
                .databaseBuilder(context, DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                .addCallback(
                        new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                Executors.newSingleThreadScheduledExecutor().execute(() -> initData(context));
                            }
                        })
                .build();
    }

    public LibraryDao getLibraryDao() {
        return db.libraryDao();
    }

    private void initData(Context context) {
        try {
            List<BookEntity> books = new ArrayList<>();
            BookEntity book;
            book = new BookEntity();
            book.id = 0;
            book.name = "12 стульев";
            book.author = "Ильф и Петров";
            book.favorite = 1;
            books.add(book);
            book = new BookEntity();
            book.id = 1;
            book.name = "Жизнь, необыкновенные и удивительные приключения Робинзона Крузо";
            book.author = "Даниэль Дефо";
            book.favorite = 0;
            books.add(book);
            DatabaseManager.getInstance(context).getLibraryDao().insertBook(books);
        } catch (Exception e) {
            Log.e("initDatabase", e.getMessage(), e);
        }
        Log.i("initDatabase", "done");
    }
}
