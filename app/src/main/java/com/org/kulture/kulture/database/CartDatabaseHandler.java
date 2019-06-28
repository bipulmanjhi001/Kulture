package com.org.kulture.kulture.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkru on 8/18/2017.
 */

public class CartDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cartManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "cart";

    // Contacts Table Columns names
    private static final String KEY_QTY = "qty";
    private static final String KEY_NAME = "name";
    private static final String KEY_TOTAL= "total_price";
    private static final String KEY_URL="images";



    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
          + KEY_QTY + " INTEGER," + KEY_NAME + " TEXT," + KEY_TOTAL + " REAL," + KEY_URL + " TEXT" + ")";


    public CartDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);

    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addData(CartGetData contact) {
        SQLiteDatabase db = CartDatabaseHandler.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QTY,contact.get_qty());
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_TOTAL, contact.get_total_price());
        values.put(KEY_URL,contact.get_image());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    CartGetData getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_QTY,
                        KEY_NAME, KEY_TOTAL, KEY_URL }, KEY_NAME + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CartGetData contact = new CartGetData(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<CartGetData> getAllContacts() {
        List<CartGetData> contactList = new ArrayList<CartGetData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartGetData contact = new CartGetData();
                contact.set_qty(Integer.parseInt(cursor.getString(0)));
                contact.set_name(cursor.getString(1));
                contact.set_total_price(cursor.getDouble(2));
                contact.set_image(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(CartGetData contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_TOTAL, contact.get_total_price());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(contact.get_name()) });
    }

    // Deleting single contact
    public void deleteContact(CartGetData contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                new String[] { String.valueOf(contact.get_name()) });
        db.close();
    }
    //Delete all contact
    public void deleteAllContact(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_CONTACTS); //delete all rows in a table
        db.close();
    }
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
