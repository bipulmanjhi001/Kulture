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

public class RegisterDatabase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "registerManager";
    private static final String TABLE_REGISTER = "register";

    // Contacts Table Columns names
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_EMAILS = "emails";
    private static final String KEY_LNAME= "lname";
    private static final String KEY_PASS = "pass";
    private static final String KEY_CHECKS = "checks";

    public RegisterDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // todo_tag table create statement

        db.execSQL("CREATE TABLE " + TABLE_REGISTER + "(" + KEY_F_NAME + " TEXT," + KEY_EMAILS + " TEXT," + KEY_LNAME + " TEXT," + KEY_PASS + " TEXT," + KEY_CHECKS + " TEXT" + ")" );
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD REGSITER(Create, Read, Update, Delete) Operations
     */
    // Adding new contact
    public void addRegisterData(String f_name, String email, String lname, String pass, String check) {
        SQLiteDatabase db2 = RegisterDatabase.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_NAME, f_name);
        values.put(KEY_EMAILS, email);
        values.put(KEY_LNAME, lname);
        values.put(KEY_PASS, pass);
        values.put(KEY_CHECKS, check);
        db2.insert(TABLE_REGISTER, null, values);
        db2.close();
        // Closing database connection
    }

    // Getting single contact
    RegisterDataSet getRegisterContact(int id) {
        SQLiteDatabase db2 = this.getReadableDatabase();

        Cursor cursor = db2.query(TABLE_REGISTER, new String[] { KEY_F_NAME,
                        KEY_EMAILS, KEY_LNAME, KEY_PASS, KEY_CHECKS }, KEY_EMAILS + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RegisterDataSet registerDataSet = new RegisterDataSet(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        // return contact
        return registerDataSet;
    }

    // Getting All Contacts
    public List<RegisterDataSet> getAllRegisterContacts() {
        List<RegisterDataSet> registerDataSets = new ArrayList<RegisterDataSet>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGISTER;

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RegisterDataSet registerDataSet = new RegisterDataSet();
                registerDataSet.setF_name(cursor.getString(0));
                registerDataSet.setEmail(cursor.getString(1));
                registerDataSet.setLname(cursor.getString(2));
                registerDataSet.setPass(cursor.getString(3));
                registerDataSet.setCheck(cursor.getString(4));
                registerDataSets.add(registerDataSet);

            } while (cursor.moveToNext());
        }

        // return contact list
        return registerDataSets;
    }

    // Updating single contact
    public int updateRegisterContact(RegisterDataSet contact) {
        SQLiteDatabase db2 = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_NAME, contact.getF_name());
        values.put(KEY_EMAILS, contact.getEmail());
        values.put(KEY_LNAME, contact.getLname());
        values.put(KEY_PASS, contact.getPass());
        values.put(KEY_CHECKS, contact.getCheck());
        // updating row

        return db2.update(TABLE_REGISTER, values, KEY_EMAILS + " = ?",
                new String[] { String.valueOf(contact.getEmail()) });
    }

    // Deleting single contact
    public void deleteRegisterContact(RegisterDataSet contact) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.delete(TABLE_REGISTER, KEY_EMAILS + " = ?",
                new String[] { String.valueOf(contact.getEmail()) });
        db2.close();
    }
    // Getting contacts Count
    public int getRegisterContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REGISTER;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}