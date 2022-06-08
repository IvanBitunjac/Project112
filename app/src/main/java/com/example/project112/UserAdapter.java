package com.example.project112;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class UserAdapter {

    final Context context;
    UserHelper dbHelper;
    private SQLiteDatabase db;

    static final String IME_BAZE = "Users.baza";
    static final int VERSION_BAZE  = 1;

    static final String USER_TABBLE = "USER";

    static final String ID = "id";
    static final String IME = "name";
    static final String PREZIME = "surname";

    static final String CREATE_TABLE_USER =
            "create table " + USER_TABBLE +
                    " (" +
                    ID+" integer primary key autoincrement, " +
                    IME+" text not null, " +
                    PREZIME +" text not null, " +
                    " );";

    static final String DROP_TABLE_USER =
            "drop table if exists " + USER_TABBLE;

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

    public long insertUser(String ime, String prezime, String napomena){
        ContentValues vrijednosti = new ContentValues();
        vrijednosti.put(IME, ime);
        vrijednosti.put(PREZIME, prezime);

        return db.insert(USER_TABBLE, null, vrijednosti);
    }

    public boolean deleteUser(long id){
        return db.delete(USER_TABBLE, ID +" = "+id, null) > 0;
    }

    public boolean updateUser(long id, String ime, String prezime, String napomena){
        ContentValues vrijednosti = new ContentValues();
        vrijednosti.put(IME, ime);
        vrijednosti.put(PREZIME, prezime);

        return db.update(USER_TABBLE, vrijednosti, ID +" = "+id, null ) > 0;
    }

    public Cursor printAllUsers (){
        return db.query(USER_TABBLE, null, null, null, null, null, null);
    }

    public Cursor getUsersById(long id) throws SQLException{
        Cursor c = db.query(USER_TABBLE, new String[] {ID,IME,PREZIME}, ID + "=" + id, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }
}
