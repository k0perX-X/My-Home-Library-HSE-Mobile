package ru.hse.myhomelibrary.ui.book;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.hse.myhomelibrary.R;
import ru.hse.myhomelibrary.database.BookEntity;
import ru.hse.myhomelibrary.database.DatabaseViewModel;

public class BookActivity extends AppCompatActivity {

    public static final String ARG_BOOK_ENTITY = "0";
    public BookEntity bookEntity;
    private Button makeFavorite;
    private Button alreadyFavorite;
    private TextView authorTextView;
    private TextView infoTextView;
    private TextView nameTextView;
    private ImageView imageView;
    private EditText reviewEditText;

    private DatabaseViewModel db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bookEntity = (BookEntity) getIntent().getSerializableExtra(ARG_BOOK_ENTITY);
        authorTextView = findViewById(R.id.authorTextView);
        infoTextView = findViewById(R.id.infoTextView);
        nameTextView = findViewById(R.id.nameTextView);
        imageView = findViewById(R.id.imageView);
        makeFavorite = findViewById(R.id.makeFavorite);
        alreadyFavorite = findViewById(R.id.alreadyFavorite);
        reviewEditText = findViewById(R.id.editTextTextMultiLine);
        db = new DatabaseViewModel(getApplication());
        loadData();

        makeFavorite.setOnClickListener(v -> makeFavoriteOnClick());
        alreadyFavorite.setOnClickListener(v -> alreadyFavoriteOnClick());
        reviewEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateReview(s.toString());
            }
        });
    }


    private void loadData() {

        imageView.setImageURI(bookEntity.imageUri);

        nameTextView.setText(bookEntity.name);

        authorTextView.setText(bookEntity.author);

        infoTextView.setVisibility(View.VISIBLE);
        if (bookEntity.yearPublishing == null && bookEntity.placePublishing == null){
            infoTextView.setVisibility(View.INVISIBLE);
        } else if (bookEntity.yearPublishing == null) {
            infoTextView.setText(bookEntity.placePublishing);
        } else if (bookEntity.placePublishing == null) {
            infoTextView.setText(String.format("%s", bookEntity.yearPublishing));
        } else {
            infoTextView.setText(String.format("%s, %s", bookEntity.yearPublishing, bookEntity.placePublishing));
        }

        setupFavoriteButtons();

        EditText editText = findViewById(R.id.editTextTextMultiLine);
        editText.setText(bookEntity.review);
    }

    private void setupFavoriteButtons() {
        switch (bookEntity.favorite) {
            case 0:
                makeFavorite.setVisibility(View.VISIBLE);
                alreadyFavorite.setVisibility(View.INVISIBLE);
                break;
            case 1:
                makeFavorite.setVisibility(View.INVISIBLE);
                alreadyFavorite.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void makeFavoriteOnClick() {
        bookEntity.favorite = 1;
        db.updateBook(bookEntity);
        setupFavoriteButtons();
    }

    private void alreadyFavoriteOnClick() {
        bookEntity.favorite = 0;
        db.updateBook(bookEntity);
        setupFavoriteButtons();
    }

    private void updateReview(String review) {
        bookEntity.review = review;
        db.updateBook(bookEntity);
    }
}