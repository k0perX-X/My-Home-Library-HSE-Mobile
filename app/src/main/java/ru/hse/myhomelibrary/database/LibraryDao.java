package ru.hse.myhomelibrary.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

@Dao
public interface LibraryDao {
    @Query("SELECT * FROM `book`")
    LiveData<List<BookEntity>> getAllBooks();

    @Insert
    void insertBook(List<BookEntity> data);

    @Delete
    void delete(BookEntity data);

    @Transaction
    @Query("SELECT * FROM book " +
            "WHERE :date <= time_start AND :nextDate >= time_start and group_id = :groupId " +
            "ORDER BY time_start") //TODO какая сортировка?
    LiveData<List<BookEntity>> getAllFavorites();
}
