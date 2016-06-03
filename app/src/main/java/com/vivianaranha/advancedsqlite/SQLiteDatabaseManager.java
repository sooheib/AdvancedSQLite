package com.vivianaranha.advancedsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 * Created by vivianaranha on 1/27/16.
 */
public class SQLiteDatabaseManager {

    SQLHelper helper;

    public SQLiteDatabaseManager(Context context){
        helper = new SQLHelper(context);
    }

    //CRUD
    // 1 INSERT
    // 2 GET
    // 3 UPDATE
    // 4 DELETE
    public long insertData(String name, String address){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.NAME, name);
        contentValues.put(SQLHelper.ADDRESS, address);

        long id = db.insert(SQLHelper.TABLE_NAME, null, contentValues);
        db.close();

        return id;

    }

    public String getData(){
        SQLiteDatabase db = helper.getReadableDatabase();
//        "SELECT * FROM USERS"

        String[] columns = {SQLHelper.UID, SQLHelper.NAME, SQLHelper.ADDRESS};

        Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();

        /*
          *
          * 1 | Vivian |123 Main St
          * 2 | Sam | 123 Someother st
          *
          */

        while(cursor.moveToNext()){
            int indexId = cursor.getColumnIndex(SQLHelper.UID);
            int indexName = cursor.getColumnIndex(SQLHelper.NAME);
            int indexAddress = cursor.getColumnIndex(SQLHelper.ADDRESS);
            int uid = cursor.getInt(indexId);
            String name = cursor.getString(indexName);
            String address = cursor.getString(indexAddress);

            buffer.append(uid+":"+name+"-"+address+"\n");


        }

        return buffer.toString();


    }

    public String getAddress(String name){

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {SQLHelper.ADDRESS};
        String[] args = {name};

        Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns, SQLHelper.NAME+" = ?", args, null, null, null);
        StringBuffer buffer = new StringBuffer();

        /*
          *
          * 1 | Vivian |123 Main St
          * 2 | Sam | 123 Someother st
          *
          */

        while(cursor.moveToNext()){

            int indexAddress = cursor.getColumnIndex(SQLHelper.ADDRESS);

            String address = cursor.getString(indexAddress);

            buffer.append(address+"\n");

        }

        return buffer.toString();

    }

    public String getUserId(String name, String address){

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {SQLHelper.UID};
        String[] args = {name, address};

        Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns, SQLHelper.NAME+" = ? AND "+SQLHelper.ADDRESS +"= ?", args, null, null, null);
        StringBuffer buffer = new StringBuffer();

        /*
          *
          * 1 | Vivian |123 Main St
          * 2 | Sam | 123 Someother st
          *
          */

        while(cursor.moveToNext()){

            int indexId = cursor.getColumnIndex(SQLHelper.UID);
            int uid = cursor.getInt(indexId);
            buffer.append("ID:"+uid+"\n");

        }

        return buffer.toString();

    }

    public int updateName(String currentName, String newName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.NAME, newName);

        String[] args = {currentName};


        int count = db.update(SQLHelper.TABLE_NAME, contentValues, SQLHelper.NAME +" = ?", args);

        return count;
    }

    public int updateAddress(String userName, String newAddress){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.ADDRESS, newAddress);

        String[] args = {userName};


        int count = db.update(SQLHelper.TABLE_NAME, contentValues, SQLHelper.NAME +" = ?", args);

        return count;
    }

    public int deleteName(String userName){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {userName};
        int count = db.delete(SQLHelper.TABLE_NAME, SQLHelper.NAME +" = ?", args);
        return count;
    }


    public class SQLHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "usersDatabase";
        private static final int DATABASE_VERSION = 2;

        private static final String TABLE_NAME = "USERS";
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String ADDRESS = "Address";
        private static final String PHONE = "Phone";

        private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " ("+ UID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME +" VARCHAR(255), "+ADDRESS+" VARCHAR(255));";

        private static final String ALTER_TABLE = "ALTER TABLE "+ TABLE_NAME +" ADD COLUMN " + PHONE + " int DEFAULT 0";


        Context context;

        public SQLHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate called", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(ALTER_TABLE);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        }
    }
}
