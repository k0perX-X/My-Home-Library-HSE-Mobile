package ru.hse.myhomelibrary.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ru.hse.myhomelibrary.R;
import ru.hse.myhomelibrary.database.BookEntity;
import ru.hse.myhomelibrary.database.DatabaseViewModel;
import ru.hse.myhomelibrary.databinding.FragmentHomeBinding;
import ru.hse.myhomelibrary.ui.addBook.AddBookActivity;
import ru.hse.myhomelibrary.ui.book.BookActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DatabaseViewModel databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        activity = getActivity();
        assert activity != null;
        recyclerView = binding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onBookItemClick);
        FloatingActionButton addNewBookButton = binding.addNewBookButton;
        addNewBookButton.setOnClickListener(v -> addNewBookButton());

        databaseViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            setData(books);
            assert books != null;
//            Log.i("database", String.valueOf(books.size()));
//            for (BookEntity book :
//                    books) {
//                Log.i("database", book.name);
//            }
        });

        return binding.getRoot();
    }

    private void setData(List<BookEntity> outList) {
        adapter.setDataList(outList);
        recyclerView.setAdapter(adapter);
    }

    private void onBookItemClick(BookEntity bookItem) {
        Intent intent = new Intent(activity, BookActivity.class);
        BookActivity.bookEntity = bookItem;
//        intent.putExtra(BookActivity.ARG_BOOK_ENTITY, bookItem);
        Log.d("onBookItemClick", bookItem.name);
        startActivity(intent);
    }

    private void addNewBookButton() {
        Intent intent = new Intent(activity, AddBookActivity.class);
        AddBookActivity.bookEntity = null;
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static final class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<BookEntity> dataList = new ArrayList<>();
        private final OnItemClick onItemClick;

        public ItemAdapter(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        public void setDataList(List<BookEntity> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(contactView, context, onItemClick);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder ViewHolder, int position) {
            BookEntity data = dataList.get(position);
            ((BookViewHolder) ViewHolder).bind(data);
        }

    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView author;
        private final ImageView favorite;
        private final ImageView photo;
        private final ClickListener clickListener;

        public BookViewHolder(View itemView, Context context, OnItemClick onItemClick) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            author = itemView.findViewById(R.id.author);
            favorite = itemView.findViewById(R.id.favorite);
            photo = itemView.findViewById(R.id.photo);
            clickListener = new ClickListener(onItemClick);
            itemView.setOnClickListener(clickListener);
        }

        public void bind(final BookEntity data) {
            name.setText(data.name);
            author.setText(data.author);
            favorite.setVisibility(data.favorite == 1 ? View.VISIBLE : View.INVISIBLE);
            if (data.imageUri != null) {
                photo.setImageURI(data.imageUri);
            }
            clickListener.bind(data);
        }

        private static class ClickListener implements View.OnClickListener {
            private BookEntity data;
            private final OnItemClick click;

            public ClickListener(OnItemClick onItemClick) {
                click = onItemClick;
            }

            public void bind(BookEntity book) {
                data = book;
            }

            @Override
            public void onClick(View view) {
                click.onClick(data);
            }
        }
    }

    public interface OnItemClick {
        void onClick(BookEntity data);
    }

}