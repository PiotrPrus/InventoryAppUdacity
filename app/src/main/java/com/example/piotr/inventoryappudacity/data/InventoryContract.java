package com.example.piotr.inventoryappudacity.data;

import android.provider.BaseColumns;

/**
 * Created by Piotr on 04.10.2017.
 */

public class InventoryContract {

    public InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "inventory";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_PHONE = "supplier_phone";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String COLUMN_IMAGE = "image";

        public static final String CREATE_INVENTORY_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_PRICE + " TEXT NOT NULL," +
                COLUMN_QUANTITY + " INteGER NOT NULL DEFAULT 0," +
                COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL," +
                COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," +
                COLUMN_IMAGE + " TEXT NOT NULL" + ");";
    }
}
