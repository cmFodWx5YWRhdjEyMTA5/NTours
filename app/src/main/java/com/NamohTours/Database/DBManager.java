package com.NamohTours.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc, String url) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.MESSAGE, name);
        contentValue.put(DatabaseHelper.TIME, desc);
        contentValue.put(DatabaseHelper.URL, url);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.MESSAGE, DatabaseHelper.TIME, DatabaseHelper.URL, DatabaseHelper.URLTYPE};
        String orderBy = DatabaseHelper._ID + " DESC";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToNext();
        }
        return cursor;
    }

    // Insert Wish List Product Id
    public void inserWishlist(String productId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PRODUCT_ID, productId);

        database.insert(DatabaseHelper.WISHTABLE_NAME, null, contentValues);
    }

    // GET wishListProducts
    public Cursor getWishList() {
        String[] columns = new String[]{DatabaseHelper._proID, DatabaseHelper.PRODUCT_ID};
        String orderBy = DatabaseHelper._ID + " DESC";
        Cursor cursor = database.query(DatabaseHelper.WISHTABLE_NAME, columns, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToNext();
        }
        return cursor;
    }

    public ArrayList<String> getWish() {
        ArrayList<String> wish = new ArrayList<String>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[]{DatabaseHelper._proID, DatabaseHelper.PRODUCT_ID};
        String orderBy = DatabaseHelper._ID + " DESC";

        Cursor cursor = db.query(DatabaseHelper.WISHTABLE_NAME, columns, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                String product = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ID));
                wish.add(product);
            }
            while (cursor.moveToNext());
        }
        return wish;
    }

    public Cursor fetchUrl() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.URL, DatabaseHelper.URLTYPE};
        String orderBy = DatabaseHelper._ID + " DESC";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToNext();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc, String url, String UrlType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.MESSAGE, name);
        contentValues.put(DatabaseHelper.TIME, desc);
        contentValues.put(DatabaseHelper.URL, url);
        contentValues.put(DatabaseHelper.URLTYPE, UrlType);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }


    public void deleteall() {

        database.execSQL("DELETE FROM " + DatabaseHelper.TABLE_NAME);
    }

    public boolean checkAlreadyExist(String product_id) {
        String query = "SELECT product_id FROM WISHLIST WHERE product_id = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // String query = SELECT + DatabaseHelper.PRODUCT_ID+ FROM + TABLE_NAME + WHERE + YOUR_EMAIL_COLUMN + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{product_id});
        if (cursor.getCount() > 0) {

            database.delete(DatabaseHelper.WISHTABLE_NAME, DatabaseHelper.PRODUCT_ID + " = ?", new String[]{product_id});

            return true;
        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.PRODUCT_ID, product_id);
            database.insert(DatabaseHelper.WISHTABLE_NAME, null, contentValues);

            Log.e("Insert ", "Insert Product" + product_id);
            return false;
        }
    }

    public boolean checkWishListExist(String product_id) {
        String query = "SELECT product_id FROM WISHLIST WHERE product_id = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // String query = SELECT + DatabaseHelper.PRODUCT_ID+ FROM + TABLE_NAME + WHERE + YOUR_EMAIL_COLUMN + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{product_id});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;

        }
    }

}
