package com.example.piotr.inventoryappudacity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.piotr.inventoryappudacity.data.InventoryContract.InventoryEntry.*;

/**
 * Created by Piotr on 04.10.2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "inventory.db";
    public final static int DB_VERSION = 1;
    public final static String TAG = InventoryDbHelper.class.getSimpleName();

    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertItem(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getItemName());
        values.put(COLUMN_PRICE, item.getPrice());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(COLUMN_SUPPLIER_PHONE, item.getSupplierPhone());
        values.put(COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());
        values.put(COLUMN_IMAGE, item.getImage());
        long id = db.insert(TABLE_NAME, null, values);
        Log.i(TAG, "The item has been added with id: " + id);
    }

    public Cursor readInventory() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                _ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_QUANTITY,
                COLUMN_SUPPLIER_NAME, COLUMN_SUPPLIER_PHONE, COLUMN_SUPPLIER_EMAIL, COLUMN_IMAGE
        };
        return db.query(TABLE_NAME, projection, null, null, null, null, null);
    }

    public Cursor readItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                _ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_QUANTITY,
                COLUMN_SUPPLIER_NAME, COLUMN_SUPPLIER_PHONE, COLUMN_SUPPLIER_EMAIL, COLUMN_IMAGE
        };
        String selection = _ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};

        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public void updateItem(long currentItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quantity);
        String selection = _ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(currentItemId)};
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void sellOneItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity - 1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);
        String selection = _ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }
}
