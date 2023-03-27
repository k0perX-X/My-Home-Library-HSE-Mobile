package ru.hse.myhomelibrary.ui.home;

import android.widget.ScrollView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    //private final MutableLiveData<String> mText;
    private MutableLiveData<ScrollView> mScroll = new MutableLiveData<>();

    public HomeViewModel() {
        //mText = new MutableLiveData<>();
        mScroll = new MutableLiveData<>();
        //mText.setValue("This is home fragment");

    }

    public void setScroll(ScrollView myScrollView){
        mScroll.setValue(myScrollView);
    }

    //public LiveData<String> getText() {
       // return mText;
    //}
}