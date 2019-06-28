package com.org.kulture.kulture.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkru on 8/30/2017.
 */

public class BillingDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "billingManager";
    private static final String TABLE_BILLING = "billing";

    // Contacts Table Columns names
    private static final String KEY_F_NAME = "firstname";
    private static final String KEY_L_NAME = "lastname";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_EMAILS = "emails";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PIN = "pincode";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE="state";

    public BillingDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // todo_tag table create statement                                                                        /*
   db.execSQL("CREATE TABLE " + TABLE_BILLING + "(" + KEY_F_NAME + " TEXT," + KEY_L_NAME + " TEXT," + KEY_COMPANY + " TEXT," + KEY_COUNTRY + " TEXT," + KEY_EMAILS + " TEXT," + KEY_MOBILE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_PIN + " INTEGER," + KEY_CITY + " TEXT," + KEY_STATE + " TEXT" + ")" );
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLING);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD REGISTER(Create, Read, Update, Delete) Operations
     */
    // Adding new contact
    public void addBillingData(String first_name, String last_name, String company, String country, String email, String mob_number, String address, int pincode, String city, String state) {
        SQLiteDatabase db2 = BillingDatabase.this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_NAME, first_name);
        values.put(KEY_L_NAME, last_name);
        values.put(KEY_COMPANY, company);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_EMAILS, email);
        values.put(KEY_MOBILE, mob_number);
        values.put(KEY_ADDRESS,address);
        values.put(KEY_PIN,pincode);
        values.put(KEY_CITY,city);
        values.put(KEY_STATE,state);

        db2.insert(TABLE_BILLING, null, values);
        db2.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<BillingDataSet> getAllBillingContacts() {
        List<BillingDataSet> billingDataSets = new ArrayList<BillingDataSet>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BILLING;

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BillingDataSet billingDataSet = new BillingDataSet();

                billingDataSet.setFirst_name(cursor.getString(0));
                billingDataSet.setLast_name(cursor.getString(1));
                billingDataSet.setCompany(cursor.getString(2));
                billingDataSet.setCountry(cursor.getString(3));
                billingDataSet.setEmail(cursor.getString(4));
                billingDataSet.setMob_number(cursor.getString(5));
                billingDataSet.setAddress(cursor.getString(6));
                billingDataSet.setPincode(cursor.getInt(7));
                billingDataSet.setCity(cursor.getString(8));
                billingDataSet.setState(cursor.getString(9));
                billingDataSets.add(billingDataSet);

            } while (cursor.moveToNext());
        }

        // return contact list
        return billingDataSets;
    }

    // Updating single contact
    public int updateRegisterContact(BillingDataSet billingDataSet) {
        SQLiteDatabase db2 = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_NAME, billingDataSet.getFirst_name());
        values.put(KEY_L_NAME, billingDataSet.getLast_name());
        values.put(KEY_COMPANY, billingDataSet.getCompany());
        values.put(KEY_COUNTRY, billingDataSet.getCountry());
        values.put(KEY_EMAILS, billingDataSet.getEmail());
        values.put(KEY_MOBILE, billingDataSet.getMob_number());
        values.put(KEY_ADDRESS,billingDataSet.getAddress());
        values.put(KEY_PIN,billingDataSet.getPincode());
        values.put(KEY_CITY,billingDataSet.getCity());
        values.put(KEY_STATE,billingDataSet.getState());
        // updating row

        return db2.update(TABLE_BILLING, values, KEY_PIN + " = ?",
                new String[] { String.valueOf(billingDataSet.getPincode()) });
    }

    // Deleting single contact
    public void deleteRegisterContact(CartGetData contact) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.delete(TABLE_BILLING, KEY_MOBILE + " = ?",
                new String[] { String.valueOf(contact.get_name()) });
        db2.close();
    }
    // Getting contacts Count
    public int getRegisterContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BILLING;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
