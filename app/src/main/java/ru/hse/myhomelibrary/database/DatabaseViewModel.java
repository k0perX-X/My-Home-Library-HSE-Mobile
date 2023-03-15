package ru.hse.myhomelibrary.database;

import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {
    private final LibraryRepository repository;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repository = new LibraryRepository(application);
    }

    public LiveData<List<BookEntity>> getAllBooks() {
        return repository.getAllBooks();
    }

    public void insertBook(List<BookEntity> books) {
        repository.insertBook(books);
    }


    public void deleteBook(BookEntity book) {
        repository.deleteBook(book);
    }

    public LiveData<List<BookEntity>> getAllFavorites() {
        return repository.getAllFavorites();
    }

    public LiveData<List<BookEntity>> getBookById(int id) {
        return repository.getBookById(id);
    }

}

