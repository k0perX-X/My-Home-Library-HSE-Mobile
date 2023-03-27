package ru.hse.myhomelibrary.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

public class LibraryRepository {

    private final LibraryDao libraryDao;

    public LibraryRepository(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        libraryDao = databaseManager.getLibraryDao();
    }

    public LiveData<List<BookEntity>> getAllBooks() {
        return libraryDao.getAllBooks();
    }

    public void insertBook(List<BookEntity> books) {
        libraryDao.insertBook(books);
    }


    public void deleteBook(BookEntity book) {
        libraryDao.deleteBook(book);
    }

    public LiveData<List<BookEntity>> getAllFavorites() {
        return libraryDao.getAllFavorites();
    }

    public LiveData<List<BookEntity>> getBookById(int id) {
        return libraryDao.getBookById(id);
    }
    public void updateBook(BookEntity book) {
        book.editDate = new Date();
        libraryDao.updateBook(book);
    }
}
