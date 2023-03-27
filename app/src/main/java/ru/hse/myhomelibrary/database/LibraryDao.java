package ru.hse.myhomelibrary.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LibraryDao {
    @Query("SELECT * FROM `book`")
    LiveData<List<BookEntity>> getAllBooks();

    @Insert
    void insertBook(List<BookEntity> data);

    @Delete
    void deleteBook(BookEntity data);

    @Query("SELECT * FROM book " +
            "WHERE favorite != 0 " +
            "ORDER BY date_of_create")
    LiveData<List<BookEntity>> getAllFavorites();

    @Transaction
    @Query("SELECT * FROM book " +
            "WHERE id = :id")
    LiveData<List<BookEntity>> getBookById(int id);

    @Update
    void updateBook(BookEntity data);

}
