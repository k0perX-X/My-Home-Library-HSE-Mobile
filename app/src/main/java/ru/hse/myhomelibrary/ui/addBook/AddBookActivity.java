package ru.hse.myhomelibrary.ui.addBook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import ru.hse.myhomelibrary.BuildConfig;
import ru.hse.myhomelibrary.R;
import ru.hse.myhomelibrary.database.BookEntity;
import ru.hse.myhomelibrary.database.DatabaseViewModel;
import ru.hse.myhomelibrary.ui.book.BookActivity;

public class AddBookActivity extends AppCompatActivity {
    //    public static final String ARG_BOOK_ENTITY = "0";
    private static final String PERMISSION = Manifest.permission.CAMERA;
    private static final int REQUEST_PERMISSION_CODE = 100;
    public static BookEntity bookEntity;

    private ImageView imageView;
    private EditText editTextName;

    private DatabaseViewModel db;
    private String saveFolder;
    private String imageName;
    private boolean newBookEntity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

//        bookEntity = (BookEntity) getIntent().getSerializableExtra(ARG_BOOK_ENTITY);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        imageView = findViewById(R.id.imageView);
        editTextName = findViewById(R.id.editTextName);
        EditText editTextAuthor = findViewById(R.id.editTextAuthor);
        EditText editTextYear = findViewById(R.id.editTextYear);
        EditText editTextPlace = findViewById(R.id.editTextPlace);
        db = new DatabaseViewModel(getApplication());

        if (bookEntity == null) {
            bookEntity = new BookEntity();
            newBookEntity = true;
            setupImageName();
        } else {
            newBookEntity = false;
            editTextName.setText(bookEntity.name);
            editTextAuthor.setText(bookEntity.author);
            if (bookEntity.yearPublishing != null) {
                editTextYear.setText(String.format("%s", bookEntity.yearPublishing));
            }
            if (bookEntity.placePublishing != null) {
                editTextPlace.setText(bookEntity.placePublishing);
            }
            if (bookEntity.imageUri != null) {
                imageView.setImageURI(bookEntity.imageUri);
                setupImageName(bookEntity.imageUri);
            } else {
                setupImageName();
            }
        }

        saveFolder = getApplication().getExternalFilesDir(null).getPath();

        floatingActionButton.setOnClickListener(v -> floatingActionButtonOnClick());
        constraintLayout.setOnClickListener(v -> takePhotoClick());
        editTextName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookEntity.name = s.toString();
            }
        });
        editTextAuthor.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookEntity.author = s.toString();
            }
        });
        editTextYear.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookEntity.yearPublishing = Integer.parseInt(s.toString());
            }
        });
        editTextPlace.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookEntity.placePublishing = s.toString();
            }
        });
    }

    private void setupImageName() {
        imageName = String.format("%s.jpg", System.currentTimeMillis());
        Log.i("onCreateAddBookActivityNewImageName", imageName);
    }

    private void setupImageName(Uri uri) {
        imageName = uri.getLastPathSegment();
        Log.i("onCreateAddBookActivityOldImageName", imageName);
    }

    private void floatingActionButtonOnClick() {
        if (editTextName.getText().toString().equals("") || editTextName.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Название книги и Автор обязательные поля", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (newBookEntity) {
                db.insertBook(Collections.singletonList(bookEntity));
                Intent intent = new Intent(this, BookActivity.class);
                BookActivity.bookEntity = bookEntity;
//                intent.putExtra(BookActivity.ARG_BOOK_ENTITY, bookEntity.id);
                Log.d("floatingActionButtonOnClick", bookEntity.name);
                startActivity(intent);
            } else {
                db.updateBook(bookEntity);
            }
            bookEntity = null;
            finish();
        }
    }

    private void takePhotoClick() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                showDialogNotPermittedCamera();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
            }
        } else {
            createImageFile();
        }
    }

    private void showDialogNotPermittedCamera() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.alertSettingsDialogMessage);
        dlgAlert.setTitle(R.string.alertSettingsDialogTitle);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton(R.string.alertSettingsDialogOK,
                (dialog, which) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0));
        dlgAlert.create().show();
    }

    private void createImageFile() {
        try {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, GetUriFromPath(saveFolder, imageName));
            openSomeActivityForResult(takePhotoIntent);
        } catch (IOException e) {
            Log.e(TAG, "Create file", e);
        }
    }

    public void openSomeActivityForResult(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    setImageViewPicture();
                }
            });

    private void setImageViewPicture() {
        try {
            Uri uri = GetUriFromPath(saveFolder, imageName);
            imageView.setImageURI(uri);
            bookEntity.imageUri = uri;
        } catch (IOException ex) {
            Log.e(TAG, "Create file", ex);
        }
    }

    private Uri GetUriFromPath(String dir, String fileName) throws IOException {
        File file = new File(dir, fileName);
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdir();
        return FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider", file);
    }
}