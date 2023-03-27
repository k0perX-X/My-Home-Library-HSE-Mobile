package ru.hse.myhomelibrary.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ru.hse.myhomelibrary.database.BookEntity;
import ru.hse.myhomelibrary.database.DatabaseViewModel;
import ru.hse.myhomelibrary.databinding.FragmentHomeBinding;
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        DatabaseViewModel db = new ViewModelProvider(this).get(DatabaseViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ScrollView scrollOfBooks = binding.booksScroll;

        List<BookEntity> booksInDb = db.getAllBooks().getValue();

        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int booksCount = 0;

        try{
            booksCount = booksInDb.size();
        }catch (Exception e){
            booksCount = 0;
        }

        for(int i = 0; i < booksCount; i++){
            BookEntity current_book = booksInDb.get(i);

            TextView nameOfBook = new TextView(this.getContext());
            nameOfBook.setText(current_book.name);


            linearLayout.addView(nameOfBook);

        };


        if(linearLayout.getChildCount() != 0) {
            scrollOfBooks.addView(linearLayout);

            homeViewModel.setScroll(scrollOfBooks);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}