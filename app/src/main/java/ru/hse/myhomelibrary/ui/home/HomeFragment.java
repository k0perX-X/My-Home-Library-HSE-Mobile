package ru.hse.myhomelibrary.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.hse.myhomelibrary.R;
import ru.hse.myhomelibrary.database.BookEntity;
import ru.hse.myhomelibrary.database.DatabaseViewModel;
import ru.hse.myhomelibrary.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        DatabaseViewModel databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Activity activity = getActivity();
        assert activity != null;
        recyclerView = binding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        databaseViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            setData(books);
            assert books != null;
            Log.i("database", String.valueOf(books.size()));
            for (BookEntity book:
                 books) {
                Log.i("database" , book.name);
            }
        });

        return binding.getRoot();
    }

    private void setData(List<BookEntity> outList) {
        adapter.setDataList(outList);
        recyclerView.setAdapter(adapter);
    }

    private void onScheduleItemClick(BookEntity bookItem) {
        //TODO
    }
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.fragment_home_menu, menu);
//    }

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