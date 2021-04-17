package com.example.inventorymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBAdapter extends SQLiteOpenHelper {

    SQLiteDatabase database;

    public static final String DATABASE = "inventorydata_1.db";
    public static final String INVENTORY = "inventory_1";

    private static final String CREATE_INVENTORY = "create table IF NOT EXISTS " + INVENTORY +
            "(_id integer PRIMARY KEY AUTOINCREMENT, product_name text, supplier_name text, mobile text, quantity int, price int)";

    public static final int VERSION = 1;
    private Context ctx;

    public DBAdapter(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, factory, VERSION);
        // Database will be created here.
        ctx = context;
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    // This method will run only first time we run project in mobile or after uninstalling app from mobile. So below log.d(CREATE_INVENTORY)
    // and log.d("Table Created") will be shown only first time.

        // this method will be called before above constructor, so "db" object of SQLiteDatabase class (as mentiond this method argument) is used to execute execSQL() method, instead of "database" object.
        // Tables will be created here.
        log.d(CREATE_INVENTORY);
        try
        {
            db.execSQL(CREATE_INVENTORY);      // table will be created
        }
        catch(SQLiteException error)
        {
            log.e(error.getMessage());
            Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        log.d("Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         sqLiteDatabase.execSQL("drop table IF EXISTS " + INVENTORY);
         onCreate(sqLiteDatabase);
    }

    public boolean insert_data(String productname, String suppliername, String mobile, int price, int quantity) {
        // This method will be called from onClickListener of btnadd and response(true or false) will be returned to it.
        // Insert Query(Second approach)
        ContentValues values = new ContentValues();
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("product_name", productname);
        values.put("supplier_name", suppliername);
        values.put("mobile", mobile);

        long response = database.insert(INVENTORY,null,values);
        if(response == -1)    // Bcoz insert query returns -1, if error occurs.
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean edit_data(String productname, String suppliername, String mobile, int price, int quantity, String Conditions[])
    {
        // Update query (second approach).
        ContentValues values = new ContentValues();
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("product_name", productname);
        values.put("supplier_name", suppliername);
        values.put("mobile", mobile);

        int response1 = database.update(INVENTORY, values, "_id=?", Conditions);
        if(response1>=1)    // Bcoz update query returns the number of rows affected.
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Cursor view_data()
    {
        // Select query
        Cursor cursor = database.rawQuery("select * from " + INVENTORY, null);    // First approach
        return cursor;
       /* String FieldList[] = {"_id","product_name","supplier_name","mobile","price","quantity"};   // Second approach
        Cursor cursor = database.query(DBAdapter.INVENTORY, FieldList,"_id=?", Condition, null, null, null);
        return cursor;*/
    }

    public Cursor view_data_for_edit(String FieldList[], String Condition[])
    {
        // Select query (Second approach).
        Cursor cursor2 = database.query(INVENTORY, FieldList, "_id=?", Condition, null, null, null);
        return cursor2;
    }

    public void delete_data(String[] Condition)
    {
        // Delete query (Second approach)
        database.delete(INVENTORY,"_id=?",Condition);
    }
}
