package ru.hse.myhomelibrary.database;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.room.TypeConverter;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import ru.hse.myhomelibrary.BuildConfig;
import ru.hse.myhomelibrary.MyApp;


public class Converters {
    @TypeConverter
    public static Uri fromPath(String value) {
        try {
            return value == null ? null : GetUriFromPath(value);
        } catch (IOException e) {
            Log.e("Convert uriFromPath", e.getMessage());
            return null;
        }
    }

    private static Uri GetUriFromPath(String path) throws IOException {
        File file = new File(path);
        Context context = MyApp.getContext();
        return FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider", file);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.getPath();
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
