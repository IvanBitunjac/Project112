package com.example.project112;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;

import java.sql.SQLException;
import java.util.Date;

public class UserAdapter {

    final Context context;
    UserHelper dbHelper;
    private SQLiteDatabase db;

    static final String IME_BAZE = "Users.baza";
    static final int VERSION_BAZE  = 1;

    static final String USER_TABLE = "USER";

    static final String ID = "id";
    static final String NAME = "name";
    static final String LASTNAME = "lastName";
    static final String DATEOFBIRTH = "date";
    static final String CNUMBER = "cnumber";
    static final String COUNTRY = "country";
    static final String MEDICATIONS = "medication";
    static final String ALLERGIES = "allergies";

    static final String CREATE_TABLE_USER =
            "create table " + USER_TABLE +
                " (" +
                    ID + " integer primary key autoincrement, " +
                    NAME + " text not null, " +
                    LASTNAME + " text not null, " +
                    DATEOFBIRTH + " text, " +
                    CNUMBER + " text, " +
                    COUNTRY + " text, " +
                    MEDICATIONS + " text, " +
                    ALLERGIES + " text " +
                " );";

    static final String DROP_TABLE_USER =
            "drop table if exists " + USER_TABLE;

    public UserAdapter(Context context) {
        this.context = context;
        dbHelper = new UserHelper(context);
    }

    public UserAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close (){
        dbHelper.close();
    }

    public long insertUser(String name, String lastName, String cnumber, String country, String medication, String allergies){
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(LASTNAME, lastName);
        values.put(CNUMBER, cnumber);

        //vrijednosti.put(DATEOFBIRTH, dateOfBirth);

        values.put(COUNTRY, country);
        values.put(MEDICATIONS, medication);
        values.put(ALLERGIES, allergies);

        return db.insert(USER_TABLE, null, values);
    }

    public boolean deleteUser(long id){
        return db.delete(USER_TABLE, ID +" = "+id, null) > 0;
    }

    public boolean updateUser(long id, String name, String lastName, String cnumber, String country, String medication, String allergies){
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(LASTNAME, lastName);
        values.put(CNUMBER, cnumber);

        //vrijednosti.put(DATEOFBIRTH, dateOfBirth);

        values.put(COUNTRY, country);
        values.put(MEDICATIONS, medication);
        values.put(ALLERGIES, allergies);

        return db.update(USER_TABLE, values, ID +" = "+id, null ) > 0;
    }

    public Cursor printAllUsers (){
        return db.query(USER_TABLE, null, null, null, null, null, null);
    }

    public Cursor getUsersByName(String name) throws SQLException{
        Cursor c = db.query(USER_TABLE, new String[] {ID,NAME,LASTNAME}, NAME + "=" + name, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }
}
