package com.example.project112;

import static com.example.project112.UserAdapter.CREATE_TABLE_USER;
import static com.example.project112.UserAdapter.DROP_TABLE_USER;
import static com.example.project112.UserAdapter.IME_BAZE;
import static com.example.project112.UserAdapter.VERSION_BAZE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserHelper extends SQLiteOpenHelper {

    public UserHelper(@Nullable Context context) {
        super(context, IME_BAZE, null, VERSION_BAZE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w("FakultetDB", "DB Upgrade iz verzije " + oldVersion + " u novu verziju " + newVersion);
        sqLiteDatabase.execSQL(DROP_TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
