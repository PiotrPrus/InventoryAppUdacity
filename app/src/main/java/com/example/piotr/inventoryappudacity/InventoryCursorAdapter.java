package com.example.piotr.inventoryappudacity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.piotr.inventoryappudacity.data.InventoryContract.InventoryEntry.*;


/**
 * Created by Piotr on 04.10.2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    private final MainActivity mainActivity;

    public InventoryCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.mainActivity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name_text_view);
        TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        ImageView imageView = view.findViewById(R.id.item_image_view);
        Button saleButton = view.findViewById(R.id.sale_button);

        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));

        imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))));

        nameTextView.setText(name);
        quantityTextView.setText(String.valueOf(quantity));
        priceTextView.setText(price);

        final long id = cursor.getLong(cursor.getColumnIndex(_ID));

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mainActivity.clickOnViewItem(id);
            }
        });
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.clickOnSale(id, quantity);
            }
        });

    }
}
